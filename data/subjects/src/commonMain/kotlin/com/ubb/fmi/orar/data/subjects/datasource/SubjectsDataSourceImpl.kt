package com.ubb.fmi.orar.data.subjects.datasource

import Logger
import com.ubb.fmi.orar.data.database.dao.SubjectDao
import com.ubb.fmi.orar.data.database.model.SubjectEntity
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.service.SubjectsApi
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.data.timetable.model.Activity
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.Host
import com.ubb.fmi.orar.data.timetable.model.Location
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.Participant
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
import okio.ByteString.Companion.encodeUtf8

/**
 * Data source for managing subject related information
 */
class SubjectsDataSourceImpl(
    private val eventsDataSource: EventsDataSource,
    private val roomsDataSource: RoomsDataSource,
    private val subjectsApi: SubjectsApi,
    private val subjectDao: SubjectDao,
    private val logger: Logger,
) : SubjectsDataSource {

    /**
     * Retrieve list of [Owner.Subject] objects from cache or API
     * by [year] and [semesterId]
     */
    override suspend fun getSubjects(
        year: Int,
        semesterId: String,
    ): Resource<List<Owner.Subject>> {
        logger.d(TAG, "getSubjects for year: $year, semester: $semesterId")

        val configurationId = year.toString() + semesterId
        val cachedSubjects = getSubjectsFromCache(configurationId)

        return when {
            cachedSubjects.isNotEmpty() -> {
                val sortedSubjects = sortSubjects(cachedSubjects)
                logger.d(TAG, "getSubjects from cache: $sortedSubjects")
                Resource(sortedSubjects, Status.Success)
            }

            else -> {
                val subjectsResource = getSubjectsFromApi(year, semesterId)
                subjectsResource.payload?.forEach { saveSubjectInCache(it) }

                val sortedSubjects = subjectsResource.payload?.let(::sortSubjects)
                logger.d(TAG, "getSubjects from API: $sortedSubjects, ${subjectsResource.status}")

                Resource(sortedSubjects, subjectsResource.status)
            }
        }
    }

    /**
     * Retrieve timetable of [Owner.Subject] for specific subject from cache or
     * API by [year], [semesterId] and [subjectId]
     */
    override suspend fun getTimetable(
        year: Int,
        semesterId: String,
        subjectId: String,
    ): Resource<Timetable<Owner.Subject>> {
        logger.d(TAG, "getTimetable for year: $year, semester: $semesterId, subject: $subjectId")

        val configurationId = year.toString() + semesterId
        val resource = getSubjects(year, semesterId)
        val subject = resource.payload?.firstOrNull { it.id == subjectId }

        logger.d(TAG, "getTimetable subject: $subject")
        val cachedEvents = subject?.let {
            eventsDataSource.getEventsFromCache(configurationId, it.id)
        }

        return when {
            cachedEvents?.isNotEmpty() == true -> {
                val sortedEvents = eventsDataSource.sortEvents(cachedEvents)
                val timetable = Timetable(subject, sortedEvents)
                logger.d(TAG, "getTimetable from cache: $timetable")

                Resource(timetable, Status.Success)
            }

            else -> {
                if (subject == null) return Resource(null, resource.status)
                val timetableResource = getTimetableFromApi(year, semesterId, subject)
                timetableResource.payload?.let {
                    saveSubjectInCache(it.owner)
                    eventsDataSource.saveEventsInCache(it.events)
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
     * Invalidates all cached subjects by [year] and [semesterId]
     */
    override suspend fun invalidate(year: Int, semesterId: String) {
        logger.d(TAG, "invalidate subjects for year: $year, semester: $semesterId")
        val configurationId = year.toString() + semesterId
        subjectDao.deleteAll(configurationId)
    }

    /**
     * Retrieve list of [Owner.Subject] objects from cache by [configurationId]
     */
    private suspend fun getSubjectsFromCache(
        configurationId: String,
    ): List<Owner.Subject> {
        val entities = subjectDao.getAll(configurationId)
        return entities.map(::mapEntityToOwner)
    }

    /**
     * Saves new subject [owner] to cache
     */
    private suspend fun saveSubjectInCache(owner: Owner.Subject) {
        val entity = mapOwnerToEntity(owner)
        subjectDao.insert(entity)
    }

    /**
     * Retrieve list of [Owner.Subject] objects from API by [year] and [semesterId]
     */
    private suspend fun getSubjectsFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<Owner.Subject>> {
        logger.d(TAG, "getSubjectsFromApi for year: $year, semester: $semesterId")

        val configurationId = year.toString() + semesterId
        val resource = subjectsApi.getSubjectsHtml(year, semesterId)

        logger.d(TAG, "getSubjectsFromApi resource: $resource")

        val subjectsHtml = resource.payload
        val table = subjectsHtml?.let(HtmlParser::extractTables)?.firstOrNull()

        logger.d(TAG, "getSubjectsFromApi table: $table")

        val subjects = table?.rows?.mapNotNull { row ->
            logger.d(TAG, "getSubjectsFromApi row: $row")
            val idCell = row.cells.getOrNull(ID_INDEX) ?: return@mapNotNull null
            val nameCell = row.cells.getOrNull(NAME_INDEX) ?: return@mapNotNull null

            Owner.Subject(
                id = idCell.value,
                name = nameCell.value,
                configurationId = configurationId,
            )
        }

        logger.d(TAG, "getSubjectsFromApi subjects: $subjects")

        return when {
            subjects.isNullOrEmpty() -> Resource(null, resource.status)
            else -> Resource(subjects, Status.Success)
        }
    }

    /**
     * Retrieve timetable of [Owner.Subject] for specific subject from API
     * by [year], [semesterId] and [subject]
     */
    private suspend fun getTimetableFromApi(
        year: Int,
        semesterId: String,
        subject: Owner.Subject,
    ): Resource<Timetable<Owner.Subject>> {
        logger.d(TAG, "getTimetableFromApi for year: $year, semester: $semesterId")

        val configurationId = year.toString() + semesterId
        val resource = subjectsApi.getTimetableHtml(year, semesterId, subject.id)

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
            val teacherCell = row.cells.getOrNull(TEACHER_INDEX) ?: return@mapNotNull null
            val intervals = intervalCell.value.split(String.DASH)
            val startHour = intervals.getOrNull(START_HOUR_INDEX) ?: return@mapNotNull null
            val endHour = intervals.getOrNull(END_HOUR_INDEX) ?: return@mapNotNull null

            val id = listOf(
                dayCell.value,
                intervalCell.value,
                frequencyCell.value,
                roomCell.value,
                studyLineCell.value,
                participantCell.value,
                eventTypeCell.value,
                teacherCell.value,
            ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

            val room = roomsDataSource.getRooms(year, semesterId).payload?.firstOrNull {
                it.id == roomCell.id
            }

            val location = room?.let {
                Location(
                    id = room.id,
                    name = room.name,
                    address = room.address
                )
            }

            val activity = Activity(
                id = subject.id,
                name = subject.name
            )

            val participant = Participant(
                id = participantCell.id,
                name = participantCell.value
            )

            val host = Host(
                id = teacherCell.id,
                name = teacherCell.value
            )

            Event(
                id = id,
                configurationId = configurationId,
                ownerId = subject.id,
                day = Day.getById(dayCell.value),
                frequency = Frequency.getById(frequencyCell.value),
                startHour = startHour.toIntOrNull() ?: return@mapNotNull null,
                endHour = endHour.toIntOrNull() ?: return@mapNotNull null,
                location = location,
                activity = activity,
                type = EventType.getById(eventTypeCell.value),
                participant = participant,
                host = host,
                isVisible = true
            )
        }

        logger.d(TAG, "getTimetableFromApi events: $events")

        return when {
            events == null -> Resource(null, resource.status)
            else -> Resource(Timetable(subject, events), Status.Success)
        }
    }

    /**
     * Sorts subjects by name
     */
    private fun sortSubjects(
        subjects: List<Owner.Subject>,
    ): List<Owner.Subject> {
        return subjects.sortedBy { it.name }
    }

    /**
     * Maps a [Owner.Subject] to a [SubjectEntity]
     */
    private fun mapOwnerToEntity(owner: Owner.Subject): SubjectEntity {
        return SubjectEntity(
            id = owner.id,
            name = owner.name,
            configurationId = owner.configurationId
        )
    }

    /**
     * Maps a [SubjectEntity] to a [Owner.Subject]
     */
    private fun mapEntityToOwner(entity: SubjectEntity): Owner.Subject {
        return Owner.Subject(
            id = entity.id,
            name = entity.name,
            configurationId = entity.configurationId
        )
    }

    companion object {
        private const val TAG = "SubjectsDataSource"

        // Subjects map column indexes
        private const val ID_INDEX = 0
        private const val NAME_INDEX = 1

        // Subject timetable column indexes
        private const val DAY_INDEX = 0
        private const val INTERVAL_INDEX = 1
        private const val FREQUENCY_INDEX = 2
        private const val ROOM_INDEX = 3
        private const val STUDY_LINE_INDEX = 4
        private const val PARTICIPANT_INDEX = 5
        private const val EVENT_TYPE_INDEX = 6
        private const val TEACHER_INDEX = 7

        // Interval indexes
        private const val START_HOUR_INDEX = 0
        private const val END_HOUR_INDEX = 1
    }
}
