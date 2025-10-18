package com.ubb.fmi.orar.data.teachers.datasource

import Logger
import com.ubb.fmi.orar.data.database.dao.TeacherDao
import com.ubb.fmi.orar.data.database.model.TeacherEntity
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.service.TeachersApi
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.rooms.repository.RoomsRepository
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import okio.ByteString.Companion.encodeUtf8
import kotlin.collections.map

/**
 * Data source for managing teacher related information
 */
@Suppress("CyclomaticComplexMethod")
class TeachersDataSourceImpl(
    private val roomsRepository: RoomsRepository,
    private val teachersApi: TeachersApi,
    private val teacherDao: TeacherDao,
    private val logger: Logger,
) : TeachersDataSource {

    override fun getTeachersFromCache(
        year: Int,
        semesterId: String,
    ): Flow<List<Owner.Teacher>> {
        val configurationId = year.toString() + semesterId
        return teacherDao.getAllAsFlow(configurationId).map { entities ->
            entities.map(::mapEntityToTeacher)
        }
    }

    override suspend fun saveTeachersInCache(teachers: List<Owner.Teacher>) {
        val entities = teachers.map(::mapTeacherToEntity)
        teacherDao.insertAll(entities)
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
     * Retrieve list of [Owner.Teacher] objects from API by [year] and [semesterId]
     */
    override suspend fun getTeachersFromApi(
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

    override suspend fun getEventsFromApi(
        year: Int,
        semesterId: String,
        teacher: Owner.Teacher,
    ): Resource<List<Event>> {
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

            val room = roomsRepository.getRooms().firstOrNull()?.payload?.firstOrNull {
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

        val status = when {
            events?.isEmpty() == true -> Status.Empty
            else -> resource.status
        }

        return Resource(events, status)
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
