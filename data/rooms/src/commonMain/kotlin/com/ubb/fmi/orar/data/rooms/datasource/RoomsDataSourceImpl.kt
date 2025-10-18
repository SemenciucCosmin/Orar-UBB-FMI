package com.ubb.fmi.orar.data.rooms.datasource

import Logger
import com.ubb.fmi.orar.data.database.dao.RoomDao
import com.ubb.fmi.orar.data.database.model.RoomEntity
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.service.RoomsApi
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.ByteString.Companion.encodeUtf8

/**
 * Data source for managing room related information
 */
class RoomsDataSourceImpl(
    private val roomsApi: RoomsApi,
    private val roomDao: RoomDao,
    private val logger: Logger,
) : RoomsDataSource {

    override fun getRoomsFromCache(
        year: Int,
        semesterId: String,
    ): Flow<List<Owner.Room>> {
        val configurationId = year.toString() + semesterId
        return roomDao.getAllAsFlow(configurationId).map { entities ->
            entities.map(::mapEntityToRoom)
        }
    }

    override suspend fun saveRoomsInCache(rooms: List<Owner.Room>) {
        val entities = rooms.map(::mapRoomToEntity)
        roomDao.insertAll(entities)
    }

    /**
     * Invalidates all cached rooms by [year] and [semesterId]
     */
    override suspend fun invalidate(year: Int, semesterId: String) {
        logger.d(TAG, "invalidate rooms for year: $year, semester: $semesterId")
        val configurationId = year.toString() + semesterId
        roomDao.deleteAll(configurationId)
    }

    /**
     * Retrieve list of [Owner.Room] objects from API by [year] and [semesterId]
     */
    override suspend fun getRoomsFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<Owner.Room>> {
        logger.d(TAG, "getRoomsFromApi for year: $year, semester: $semesterId")

        val configurationId = year.toString() + semesterId
        val resource = roomsApi.getRoomsHtml(year, semesterId)

        logger.d(TAG, "getRoomsFromApi resource: $resource")

        val roomsHtml = resource.payload
        val table = roomsHtml?.let(HtmlParser::extractTables)?.firstOrNull()

        logger.d(TAG, "getRoomsFromApi table: $table")

        val rooms = table?.rows?.mapNotNull { row ->
            logger.d(TAG, "getRoomsFromApi row: $row")
            val nameCell = row.cells.getOrNull(NAME_INDEX) ?: return@mapNotNull null
            val locationCell = row.cells.getOrNull(LOCATION_INDEX) ?: return@mapNotNull null

            Owner.Room(
                id = nameCell.id,
                name = nameCell.value,
                address = locationCell.value,
                configurationId = configurationId,
            )
        }

        logger.d(TAG, "getRoomsFromApi rooms: $rooms")

        return when {
            rooms.isNullOrEmpty() -> Resource(null, resource.status)
            else -> Resource(rooms, Status.Success)
        }
    }

    override suspend fun getEventsFromApi(
        year: Int,
        semesterId: String,
        room: Owner.Room,
    ): Resource<List<Event>> {
        logger.d(TAG, "getTimetableFromApi for year: $year, semester: $semesterId, room: $room")

        val configurationId = year.toString() + semesterId
        val resource = roomsApi.getTimetableHtml(year, semesterId, room.id)

        logger.d(TAG, "getTimetableFromApi resource: $resource")

        val timetableHtml = resource.payload
        val table = timetableHtml?.let(HtmlParser::extractTables)?.firstOrNull()

        logger.d(TAG, "getTimetableFromApi table: $table")

        val events = table?.rows?.mapNotNull { row ->
            logger.d(TAG, "getTimetableFromApi row: $row")
            val dayCell = row.cells.getOrNull(DAY_INDEX) ?: return@mapNotNull null
            val intervalCell = row.cells.getOrNull(INTERVAL_INDEX) ?: return@mapNotNull null
            val frequencyCell = row.cells.getOrNull(FREQUENCY_INDEX) ?: return@mapNotNull null
            val studyLineCell = row.cells.getOrNull(STUDY_LINE_INDEX) ?: return@mapNotNull null
            val participantCell = row.cells.getOrNull(PARTICIPANT_INDEX) ?: return@mapNotNull null
            val eventTypeCell = row.cells.getOrNull(EVENT_TYPE_INDEX) ?: return@mapNotNull null
            val subjectCell = row.cells.getOrNull(SUBJECT_INDEX) ?: return@mapNotNull null
            val teacherCell = row.cells.getOrNull(TEACHER_INDEX) ?: return@mapNotNull null
            val intervals = intervalCell.value.split(String.DASH)
            val startHour = intervals.getOrNull(START_HOUR_INDEX) ?: return@mapNotNull null
            val endHour = intervals.getOrNull(END_HOUR_INDEX) ?: return@mapNotNull null

            val id = listOf(
                dayCell.value,
                intervalCell.value,
                frequencyCell.value,
                studyLineCell.value,
                participantCell.value,
                eventTypeCell.value,
                subjectCell.value,
                teacherCell.value,
            ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

            Event(
                id = id,
                configurationId = configurationId,
                day = Day.getById(dayCell.value),
                frequency = Frequency.getById(frequencyCell.value),
                startHour = startHour.toIntOrNull() ?: return@mapNotNull null,
                endHour = endHour.toIntOrNull() ?: return@mapNotNull null,
                location = room.name,
                activity = subjectCell.value,
                type = EventType.getById(eventTypeCell.value),
                participant = participantCell.value,
                caption = teacherCell.value,
                details = room.address,
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
     * Maps a [Owner.Room] to a [RoomEntity]
     */
    private fun mapRoomToEntity(owner: Owner.Room): RoomEntity {
        return RoomEntity(
            id = owner.id,
            name = owner.name,
            location = owner.address,
            configurationId = owner.configurationId
        )
    }

    /**
     * Maps a [RoomEntity] to a [Owner.Room]
     */
    private fun mapEntityToRoom(entity: RoomEntity): Owner.Room {
        return Owner.Room(
            id = entity.id,
            name = entity.name,
            address = entity.location,
            configurationId = entity.configurationId
        )
    }

    companion object {
        private const val TAG = "RoomsDataSource"

        // Rooms map column indexes
        private const val NAME_INDEX = 0
        private const val LOCATION_INDEX = 1

        // Room timetable column indexes
        private const val DAY_INDEX = 0
        private const val INTERVAL_INDEX = 1
        private const val FREQUENCY_INDEX = 2
        private const val STUDY_LINE_INDEX = 3
        private const val PARTICIPANT_INDEX = 4
        private const val EVENT_TYPE_INDEX = 5
        private const val SUBJECT_INDEX = 6
        private const val TEACHER_INDEX = 7

        // Interval indexes
        private const val START_HOUR_INDEX = 0
        private const val END_HOUR_INDEX = 1
    }
}
