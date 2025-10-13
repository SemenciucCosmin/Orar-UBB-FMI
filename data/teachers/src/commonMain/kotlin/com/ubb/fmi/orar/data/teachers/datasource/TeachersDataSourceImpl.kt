package com.ubb.fmi.orar.data.teachers.datasource

import Logger
import com.ubb.fmi.orar.data.database.dao.TeacherDao
import com.ubb.fmi.orar.data.database.model.TeacherEntity
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.service.TeachersApi
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.TeacherTitle
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.COLON
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
import okio.ByteString.Companion.encodeUtf8

/**
 * Data source for managing teacher related information
 */
@Suppress("CyclomaticComplexMethod")
class TeachersDataSourceImpl(
    private val eventsDataSource: EventsDataSource,
    private val roomsDataSource: RoomsDataSource,
    private val teachersApi: TeachersApi,
    private val teacherDao: TeacherDao,
    private val logger: Logger,
) : TeachersDataSource {

    /**
     * Retrieve list of [Owner.Teacher] objects from cache or API
     * by [year] and [semesterId]
     */
    override suspend fun getTeachers(
        year: Int,
        semesterId: String,
    ): Resource<List<Owner.Teacher>> {
        logger.d(TAG, "getTeachers for year: $year, semester: $semesterId")

        val configurationId = year.toString() + semesterId
        val cachedTeachers = getTeachersFromCache(configurationId)

        return when {
            cachedTeachers.isNotEmpty() -> {
                val sortedTeachers = sortTeachers(cachedTeachers)
                logger.d(TAG, "getTeachers from cache: $sortedTeachers")
                Resource(sortedTeachers, Status.Success)
            }

            else -> {
                val teacherResource = getTeachersFromApi(year, semesterId)
                teacherResource.payload?.forEach { saveTeacherInCache(it) }

                val sortedTeachers = teacherResource.payload?.let(::sortTeachers)
                logger.d(TAG, "getTeachers from API: $sortedTeachers, ${teacherResource.status}")

                Resource(sortedTeachers, teacherResource.status)
            }
        }
    }

    /**
     * Retrieve timetable of [Owner.Teacher] for specific teacher from cache or
     * API by [year], [semesterId] and [teacherId]
     */
    override suspend fun getTimetable(
        year: Int,
        semesterId: String,
        teacherId: String,
    ): Resource<Timetable<Owner.Teacher>> {
        logger.d(TAG, "getTimetable for year: $year, semester: $semesterId, teacher: $teacherId")

        val configurationId = year.toString() + semesterId
        val resource = getTeachers(year, semesterId)
        val teacher = resource.payload?.firstOrNull { it.id == teacherId }

        logger.d(TAG, "getTimetable teacher: $teacher")
        val cachedEvents = teacher?.let {
            eventsDataSource.getEventsFromCache(configurationId, it.id)
        }

        return when {
            cachedEvents?.isNotEmpty() == true -> {
                val sortedEvents = eventsDataSource.sortEvents(cachedEvents)
                val timetable = Timetable(teacher, sortedEvents)
                logger.d(TAG, "getTimetable from cache: $timetable")

                Resource(timetable, Status.Success)
            }

            else -> {
                if (teacher == null) return Resource(null, resource.status)
                val timetableResource = getTimetableFromApi(year, semesterId, teacher)
                timetableResource.payload?.let {
                    saveTeacherInCache(it.owner)
                    eventsDataSource.saveEventsInCache(teacher.id, it.events)
                }

                val timetable = timetableResource.payload?.let { timetable ->
                    val sortedEvents = eventsDataSource.sortEvents(timetable.events)
                    timetable.copy(events = sortedEvents)
                }

                logger.d(TAG, "getTimetable from API: $timetable ${timetableResource.status}")
                Resource(timetable, timetableResource.status)
            }
        }
    }

    /**
     * Invalidates all cached teachers by [year] and [semesterId]
     */
    override suspend fun invalidate(year: Int, semesterId: String) {
        logger.d(TAG, "invalidate teachers for year: $year, semester: $semesterId")
        val configurationId = year.toString() + semesterId
        teacherDao.deleteAll(configurationId)
    }

    /**
     * Retrieve list of [Owner.Teacher] objects from cache by [configurationId]
     */
    private suspend fun getTeachersFromCache(
        configurationId: String,
    ): List<Owner.Teacher> {
        val entities = teacherDao.getAll(configurationId)
        return entities.map(::mapEntityToTeacher)
    }

    /**
     * Saves new [teacher] to cache
     */
    private suspend fun saveTeacherInCache(teacher: Owner.Teacher) {
        val entity = mapTeacherToEntity(teacher)
        teacherDao.insert(entity)
    }

    /**
     * Retrieve list of [Owner.Teacher] objects from API by [year] and [semesterId]
     */
    private suspend fun getTeachersFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<Owner.Teacher>> {
        logger.d(TAG, "getTeachersFromApi for year: $year, semester: $semesterId")

        val configurationId = year.toString() + semesterId
        val resource = teachersApi.getTeachersHtml(year, semesterId)

        logger.d(TAG, "getTeachersFromApi resource: $resource")

        val teachersHtml = resource.payload
        val table = teachersHtml?.let(HtmlParser::extractTables)?.firstOrNull()

        logger.d(TAG, "getTeachersFromApi table: $table")

        val cells = table?.rows?.map { it.cells }?.flatten()
        val teachers = cells?.mapNotNull { cell ->
            logger.d(TAG, "getTeachersFromApi cell: $cell")

            val title = TeacherTitle.entries.firstOrNull {
                cell.value.contains(it.id)
            } ?: return@mapNotNull null

            val name = cell.value
                .replace(title.id, String.BLANK)
                .replaceFirst(String.SPACE, String.BLANK)

            Owner.Teacher(
                id = cell.id,
                name = name,
                title = title,
                configurationId = configurationId,
            )
        }?.filter { it.name != NULL }

        logger.d(TAG, "getTeachersFromApi teachers: $teachers")

        return when {
            teachers.isNullOrEmpty() -> Resource(null, resource.status)
            else -> Resource(teachers, Status.Success)
        }
    }

    /**
     * Retrieve timetable of [Owner.Teacher] for specific teacher from API
     * by [year], [semesterId] and [teacher]
     */
    private suspend fun getTimetableFromApi(
        year: Int,
        semesterId: String,
        teacher: Owner.Teacher,
    ): Resource<Timetable<Owner.Teacher>> {
        logger.d(TAG, "getTimetableFromApi for year: $year, semester: $semesterId")

        val configurationId = year.toString() + semesterId
        val resource = teachersApi.getTimetableHtml(year, semesterId, teacher.id)

        logger.d(TAG, "getTimetableFromApi resource: $resource")

        val timetableHtml = resource.payload
        val table = timetableHtml?.let(HtmlParser::extractTables)?.firstOrNull()

        logger.d(TAG, "getTimetableFromApi table: $table")

        val events = table?.rows?.mapNotNull { row ->
            logger.d(TAG, "getTimetableFromApi row: $row")
            val dayCell = row.cells.getOrNull(DAY_INDEX) ?: return@mapNotNull null
            val intervalCell = row.cells.getOrNull(INTERVAL_INDEX) ?: return@mapNotNull null
            val frequencyCell = row.cells.getOrNull(FREQUENCY_INDEX) ?: return@mapNotNull null
            val roomCell = row.cells.getOrNull(ROOM_INDEX) ?: return@mapNotNull null
            val studyLineCell = row.cells.getOrNull(STUDY_LINE_INDEX) ?: return@mapNotNull null
            val participantCell = row.cells.getOrNull(PARTICIPANT_INDEX) ?: return@mapNotNull null
            val eventTypeCell = row.cells.getOrNull(EVENT_TYPE_INDEX) ?: return@mapNotNull null
            val subjectCell = row.cells.getOrNull(SUBJECT_INDEX) ?: return@mapNotNull null
            val intervals = intervalCell.value.split(String.DASH)
            val startHour = intervals.getOrNull(START_HOUR_INDEX) ?: return@mapNotNull null
            val endHour = intervals.getOrNull(END_HOUR_INDEX) ?: return@mapNotNull null

            val participant = when {
                participantCell.value == NULL -> String.BLANK
                else -> participantCell.value
            }

            val (eventTypeId, subject) = when {
                subjectCell.value.contains(EventType.STAFF.id) -> {
                    val subject = subjectCell.value.split(
                        String.COLON
                    ).lastOrNull()?.replace(
                        String.SPACE, String.BLANK
                    ) ?: subjectCell.value

                    EventType.STAFF.id to subject
                }

                else -> eventTypeCell.value to subjectCell.value
            }

            val id = listOf(
                dayCell.value,
                intervalCell.value,
                frequencyCell.value,
                roomCell.value,
                studyLineCell.value,
                participantCell.value,
                eventTypeCell.value,
                subjectCell.value,
            ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

            val room = roomsDataSource.getRooms(year, semesterId).payload?.firstOrNull {
                it.id == roomCell.id
            }

            Event(
                id = id,
                configurationId = configurationId,
                day = Day.getById(dayCell.value),
                frequency = Frequency.getById(frequencyCell.value),
                startHour = startHour.toIntOrNull() ?: return@mapNotNull null,
                endHour = endHour.toIntOrNull() ?: return@mapNotNull null,
                location = room?.name ?: String.BLANK,
                activity = subject,
                type = EventType.getById(eventTypeId),
                participant = participant,
                caption = "${teacher.title.label} ${teacher.name}",
                details = room?.address ?: String.BLANK,
                isVisible = true
            )
        }

        logger.d(TAG, "getTimetableFromApi events: $events")

        return when {
            events == null -> Resource(null, resource.status)
            else -> Resource(Timetable(teacher, events), Status.Success)
        }
    }

    /**
     * Sorts teachers by order index and name
     */
    private fun sortTeachers(
        owners: List<Owner.Teacher>,
    ): List<Owner.Teacher> {
        return owners.sortedWith(
            compareBy<Owner.Teacher> { it.title.orderIndex }.thenBy { it.name }
        )
    }

    /**
     * Maps a [Owner.Teacher] to a [TeacherEntity]
     */
    private fun mapTeacherToEntity(owner: Owner.Teacher): TeacherEntity {
        return TeacherEntity(
            id = owner.id,
            name = owner.name,
            titleId = owner.title.id,
            configurationId = owner.configurationId
        )
    }

    /**
     * Maps a [TeacherEntity] to a [Owner.Teacher]
     */
    private fun mapEntityToTeacher(entity: TeacherEntity): Owner.Teacher {
        return Owner.Teacher(
            id = entity.id,
            name = entity.name,
            title = TeacherTitle.getById(entity.titleId),
            configurationId = entity.configurationId
        )
    }

    companion object {
        private const val TAG = "TeachersDataSource"

        // Teacher timetable column indexes
        private const val DAY_INDEX = 0
        private const val INTERVAL_INDEX = 1
        private const val FREQUENCY_INDEX = 2
        private const val ROOM_INDEX = 3
        private const val STUDY_LINE_INDEX = 4
        private const val PARTICIPANT_INDEX = 5
        private const val EVENT_TYPE_INDEX = 6
        private const val SUBJECT_INDEX = 7

        // Interval indexes
        private const val START_HOUR_INDEX = 0
        private const val END_HOUR_INDEX = 1

        // StaffId
        private const val NULL = "null"
    }
}
