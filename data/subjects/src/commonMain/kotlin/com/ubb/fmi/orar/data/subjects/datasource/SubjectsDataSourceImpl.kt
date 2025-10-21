package com.ubb.fmi.orar.data.subjects.datasource

import Logger
import com.ubb.fmi.orar.data.database.dao.SubjectDao
import com.ubb.fmi.orar.data.database.model.SubjectEntity
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.service.SubjectsApi
import com.ubb.fmi.orar.data.rooms.repository.RoomsRepository
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.htmlparser.HtmlTableParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import okio.ByteString.Companion.encodeUtf8
import kotlin.collections.map

/**
 * Data source for managing subject related information
 */
class SubjectsDataSourceImpl(
    private val roomsRepository: RoomsRepository,
    private val subjectsApi: SubjectsApi,
    private val subjectDao: SubjectDao,
    private val logger: Logger,
) : SubjectsDataSource {

    /**
     * Retrieves [Flow] of subjects from database
     */
    override fun getSubjectsFromCache(
        year: Int,
        semesterId: String,
    ): Flow<List<Owner.Subject>> {
        val configurationId = year.toString() + semesterId
        return subjectDao.getAllAsFlow(configurationId).map { entities ->
            entities.map(::mapEntityToSubject)
        }
    }

    /**
     * Saves [subjects] in database
     */
    override suspend fun saveSubjectsInCache(subjects: List<Owner.Subject>) {
        val entities = subjects.map(::mapSubjectToEntity)
        subjectDao.insertAll(entities)
    }

    /**
     * Retrieves subjects from API
     */
    override suspend fun getSubjectsFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<Owner.Subject>> {
        logger.d(TAG, "getSubjectsFromApi for year: $year, semester: $semesterId")

        val configurationId = year.toString() + semesterId
        val resource = subjectsApi.getSubjectsHtml(year, semesterId)

        logger.d(TAG, "getSubjectsFromApi resource: $resource")

        val subjectsHtml = resource.payload
        val table = subjectsHtml?.let(HtmlTableParser::extractTables)?.firstOrNull()

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
     * Retrieves subjects events from API
     */
    @Suppress("CyclomaticComplexMethod")
    override suspend fun getEventsFromApi(
        year: Int,
        semesterId: String,
        subject: Owner.Subject,
    ): Resource<List<Event>> {
        logger.d(TAG, "getTimetableFromApi for year: $year, semester: $semesterId")

        val configurationId = year.toString() + semesterId
        val resource = subjectsApi.getEventsHtml(year, semesterId, subject.id)

        logger.d(TAG, "getTimetableFromApi resource: $resource")

        val timetableHtml = resource.payload
        val table = timetableHtml?.let(HtmlTableParser::extractTables)?.firstOrNull()

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
            val startHourString = intervals.getOrNull(START_HOUR_INDEX) ?: return@mapNotNull null
            val endHourString = intervals.getOrNull(END_HOUR_INDEX) ?: return@mapNotNull null
            val startHour = startHourString.toIntOrNull() ?: return@mapNotNull null
            val endHour = endHourString.toIntOrNull() ?: return@mapNotNull null

            val frequency = when {
                frequencyCell.value == NULL -> Frequency.BOTH
                else -> Frequency.getById(frequencyCell.value)
            }

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

            val room = roomsRepository.getRooms().firstOrNull()?.payload?.firstOrNull {
                it.id == roomCell.id
            }

            Event(
                id = id,
                configurationId = configurationId,
                ownerId = subject.id,
                day = Day.getById(dayCell.value),
                frequency = frequency,
                startHour = startHour,
                startMinute = DEFAULT_MINUTES,
                endHour = endHour,
                endMinute = DEFAULT_MINUTES,
                location = room?.name ?: String.BLANK,
                activity = subject.name,
                type = EventType.getById(eventTypeCell.value),
                participant = participantCell.value,
                caption = teacherCell.value,
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
     * Invalidates all cached subjects
     */
    override suspend fun invalidate(year: Int, semesterId: String) {
        logger.d(TAG, "invalidate subjects for year: $year, semester: $semesterId")
        val configurationId = year.toString() + semesterId
        subjectDao.deleteAll(configurationId)
    }

    /**
     * Maps a [Owner.Subject] to a [SubjectEntity]
     */
    private fun mapSubjectToEntity(owner: Owner.Subject): SubjectEntity {
        return SubjectEntity(
            id = owner.id,
            name = owner.name,
            configurationId = owner.configurationId
        )
    }

    /**
     * Maps a [SubjectEntity] to a [Owner.Subject]
     */
    private fun mapEntityToSubject(entity: SubjectEntity): Owner.Subject {
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

        private const val NULL = "null"
        private const val DEFAULT_MINUTES = 0
    }
}
