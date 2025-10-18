package com.ubb.fmi.orar.data.students.datasource

import Logger
import com.ubb.fmi.orar.data.database.dao.GroupDao
import com.ubb.fmi.orar.data.database.model.GroupEntity
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.service.StudentsApi
import com.ubb.fmi.orar.data.rooms.repository.RoomsRepository
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.StudyLine
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.extensions.SLASH
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
import com.ubb.fmi.orar.domain.htmlparser.model.Table
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import okio.ByteString.Companion.encodeUtf8

/**
 * Data source for managing study line related information
 */
class GroupsDataSourceImpl(
    private val roomsRepository: RoomsRepository,
    private val studentsApi: StudentsApi,
    private val groupDao: GroupDao,
    private val logger: Logger,
) : GroupsDataSource {

    override fun getGroupsFromCache(
        year: Int,
        semesterId: String,
        studyLine: StudyLine
    ): Flow<List<Owner.Group>> {
        val configurationId = year.toString() + semesterId
        return groupDao.getAllAsFlow(configurationId, studyLine.id).map { entities ->
            entities.map { mapEntityToGroup(it, studyLine) }
        }
    }

    override suspend fun saveGroupsInCache(groups: List<Owner.Group>) {
        val entities = groups.map(::mapGroupToEntity)
        groupDao.insertAll(entities)
    }

    /**
     * Invalidates all cached groups by [year] and [semesterId]
     */
    override suspend fun invalidate(year: Int, semesterId: String) {
        logger.d(TAG, "invalidate groups for year: $year, semester: $semesterId")
        val configurationId = year.toString() + semesterId
        groupDao.deleteAll(configurationId)
    }

    /**
     * Retrieve list of [Owner.Group] objects from API by [year] and [semesterId]
     */
    override suspend fun getGroupsFromApi(
        year: Int,
        semesterId: String,
        studyLine: StudyLine,
    ): Resource<List<Owner.Group>> {
        logger.d(TAG, "getGroupsFromApi for year: $year, semester: $semesterId, studyLine: $studyLine")

        val configurationId = year.toString() + semesterId
        val resource = studentsApi.getTimetableHtml(year, semesterId, studyLine.id)

        logger.d(TAG, "getGroupsFromApi resource: $resource")

        val timetableHtml = resource.payload
        val tables = timetableHtml?.let(HtmlParser::extractTables)

        logger.d(TAG, "getGroupsFromApi tables: $tables")

        val groups = tables?.map { table ->
            table.title.substringBefore(String.SLASH).substringAfter(String.SPACE)
        }?.distinct()?.map { groupId ->
            Owner.Group(
                id = groupId,
                name = groupId,
                studyLine = studyLine,
                configurationId = configurationId
            )
        }

        logger.d(TAG, "getGroupsFromApi groupsIds: $groups")

        return when {
            groups.isNullOrEmpty() -> Resource(null, resource.status)
            else -> Resource(groups, Status.Success)
        }
    }

    override suspend fun getEventsFromApi(
        year: Int,
        semesterId: String,
        group: Owner.Group,
    ): Resource<List<Event>> {
        logger.d(TAG, "getTimetableFromApi for year: $year, semester: $semesterId, group: $group")

        val configurationId = year.toString() + semesterId
        val resource = studentsApi.getTimetableHtml(year, semesterId, group.studyLine.id)

        logger.d(TAG, "getTimetableFromApi resource: $resource")

        val timetableHtml = resource.payload
        val tables = timetableHtml?.let(HtmlParser::extractTables)

        logger.d(TAG, "getTimetableFromApi tables: $tables")

        val joinedTables = tables?.map { table ->
            val groupTitle = table.title.substringBefore(String.SLASH)
            val groupId = groupTitle.substringAfter(String.SPACE)
            table.copy(title = groupId)
        }?.groupBy { it.title }?.map { (groupId, tables) ->
            Table(
                title = groupId,
                rows = tables.flatMap { it.rows }.distinct()
            )
        }

        logger.d(TAG, "getTimetableFromApi joinedTables: $joinedTables")

        val table = joinedTables?.firstOrNull { it.title == group.id }
        logger.d(TAG, "getTimetableFromApi table: $table")

        val events = table?.rows?.mapNotNull { row ->
            logger.d(TAG, "getTimetableFromApi row: $row")
            val dayCell = row.cells.getOrNull(DAY_INDEX) ?: return@mapNotNull null
            val intervalCell = row.cells.getOrNull(INTERVAL_INDEX) ?: return@mapNotNull null
            val frequencyCell = row.cells.getOrNull(FREQUENCY_INDEX) ?: return@mapNotNull null
            val roomCell = row.cells.getOrNull(ROOM_INDEX) ?: return@mapNotNull null
            val eventTypeCell = row.cells.getOrNull(EVENT_TYPE_INDEX) ?: return@mapNotNull null
            val groupCell = row.cells.getOrNull(SUBJECT_INDEX) ?: return@mapNotNull null
            val teacherCell = row.cells.getOrNull(TEACHER_INDEX) ?: return@mapNotNull null
            val intervals = intervalCell.value.split(String.DASH)
            val startHour = intervals.getOrNull(START_HOUR_INDEX) ?: return@mapNotNull null
            val endHour = intervals.getOrNull(END_HOUR_INDEX) ?: return@mapNotNull null
            val participantCell = row.cells.getOrNull(
                PARTICIPANT_INDEX
            ) ?: return@mapNotNull null

            val id = listOf(
                dayCell.value,
                intervalCell.value,
                frequencyCell.value,
                roomCell.id,
                group.name,
                participantCell.value,
                eventTypeCell.value,
                group.id,
                groupCell.id,
                teacherCell.id,
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
                activity = groupCell.value,
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
     * Maps a [Owner.Group] to a [GroupEntity]
     */
    private fun mapGroupToEntity(group: Owner.Group): GroupEntity {
        return GroupEntity(
            id = group.id,
            name = group.name,
            configurationId = group.configurationId,
            studyLineId = group.studyLine.id
        )
    }


    /**
     * Maps a [GroupEntity] to a [Owner.Group]
     */
    private fun mapEntityToGroup(
        entity: GroupEntity,
        studyLine: StudyLine,
    ): Owner.Group {
        return Owner.Group(
            id = entity.id,
            name = entity.name,
            configurationId = entity.configurationId,
            studyLine = studyLine,
        )
    }

    companion object {
        private const val TAG = "GroupsDataSource"

        // Group timetable column indexes
        private const val DAY_INDEX = 0
        private const val INTERVAL_INDEX = 1
        private const val FREQUENCY_INDEX = 2
        private const val ROOM_INDEX = 3
        private const val PARTICIPANT_INDEX = 4
        private const val EVENT_TYPE_INDEX = 5
        private const val SUBJECT_INDEX = 6
        private const val TEACHER_INDEX = 7

        // Interval indexes
        private const val START_HOUR_INDEX = 0
        private const val END_HOUR_INDEX = 1
    }
}
