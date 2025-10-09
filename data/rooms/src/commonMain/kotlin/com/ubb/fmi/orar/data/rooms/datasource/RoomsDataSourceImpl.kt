package com.ubb.fmi.orar.data.rooms.datasource

import Logger
import com.ubb.fmi.orar.data.database.dao.RoomDao
import com.ubb.fmi.orar.data.database.model.RoomEntity
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.service.RoomsApi
import com.ubb.fmi.orar.data.timetable.datasource.TimetableClassDataSource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.model.TimetableClass
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
import okio.ByteString.Companion.encodeUtf8

/**
 * Data source for managing room related information
 */
class RoomsDataSourceImpl(
    private val timetableClassDataSource: TimetableClassDataSource,
    private val roomsApi: RoomsApi,
    private val roomDao: RoomDao,
    private val logger: Logger,
) : RoomsDataSource {

    /**
     * Retrieve list of [Owner.Room] objects from cache or API
     * by [year] and [semesterId]
     */
    override suspend fun getRooms(
        year: Int,
        semesterId: String,
    ): Resource<List<Owner.Room>> {
        logger.d(TAG, "get rooms for year: $year, semester: $semesterId")

        val configurationId = year.toString() + semesterId
        val cachedRooms = getRoomsFromCache(configurationId)

        return when {
            cachedRooms.isNotEmpty() -> {
                val sortedRooms = sortRooms(cachedRooms)
                logger.d(TAG, "get rooms from cache: $sortedRooms")
                Resource(sortedRooms, Status.Success)
            }

            else -> {
                val roomsResource = getRoomsFromApi(year, semesterId)
                roomsResource.payload?.forEach { saveRoomInCache(it) }

                val sortedRooms = roomsResource.payload?.let(::sortRooms)
                logger.d(TAG, "get rooms from API: $sortedRooms, ${roomsResource.status}")

                Resource(sortedRooms, roomsResource.status)
            }
        }
    }

    /**
     * Retrieve timetable of [Owner.Room] for specific room from cache or
     * API by [year], [semesterId] and [roomId]
     */
    override suspend fun getTimetable(
        year: Int,
        semesterId: String,
        roomId: String,
    ): Resource<Timetable<Owner.Room>> {
        logger.d(TAG, "get room timetable for year: $year, semester: $semesterId, room: $roomId")

        val configurationId = year.toString() + semesterId
        val resource = getRooms(year, semesterId)
        val room = resource.payload?.firstOrNull { it.id == roomId }

        logger.d(TAG, "get room timetable for: $room")
        val cachedClasses = room?.let {
            timetableClassDataSource.getTimetableClassesFromCache(configurationId, it.id)
        }

        return when {
            cachedClasses?.isNotEmpty() == true -> {
                val sortedClasses = timetableClassDataSource.sortTimetableClasses(cachedClasses)
                val timetable = Timetable(owner = room, classes = sortedClasses)
                logger.d(TAG, "get room timetable from cache: $timetable")

                Resource(timetable, Status.Success)
            }

            else -> {
                if (room == null) return Resource(null, resource.status)
                val timetableResource = getTimetableFromApi(year, semesterId, room)
                timetableResource.payload?.let {
                    saveRoomInCache(it.owner)
                    timetableClassDataSource.saveTimetableClassesInCache(it.classes)
                }

                val timetable = timetableResource.payload?.let { timetable ->
                    val sortedClasses = timetableClassDataSource.sortTimetableClasses(timetable.classes)
                    timetable.copy(classes = sortedClasses)
                }

                logger.d(TAG, "get room timetable from API: $timetable ${timetableResource.status}")
                Resource(timetable, timetableResource.status)
            }
        }
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
     * Retrieve list of [Owner.Room] objects from cache by [configurationId]
     */
    private suspend fun getRoomsFromCache(
        configurationId: String,
    ): List<Owner.Room> {
        val entities = roomDao.getAll(configurationId)
        return entities.map(::mapEntityToRoom)
    }

    /**
     * Saves new room [Owner.Room] to cache
     */
    private suspend fun saveRoomInCache(room: Owner.Room) {
        val entity = mapRoomToEntity(room)
        roomDao.insert(entity)
    }

    /**
     * Retrieve list of [Owner.Room] objects from API by [year] and [semesterId]
     */
    private suspend fun getRoomsFromApi(
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
                location = locationCell.value,
                configurationId = configurationId,
            )
        }

        logger.d(TAG, "getRoomsFromApi rooms: $rooms")

        return when {
            rooms.isNullOrEmpty() -> Resource(null, resource.status)
            else -> Resource(rooms, Status.Success)
        }
    }

    /**
     * Retrieve timetable of [Owner.Room] for specific room from API
     * by [year], [semesterId] and [room]
     */
    private suspend fun getTimetableFromApi(
        year: Int,
        semesterId: String,
        room: Owner.Room,
    ): Resource<Timetable<Owner.Room>> {
        logger.d(TAG, "getTimetableFromApi for year: $year, semester: $semesterId, room: $room")

        val configurationId = year.toString() + semesterId
        val resource = roomsApi.getTimetableHtml(year, semesterId, room.id)

        logger.d(TAG, "getTimetableFromApi resource: $resource")

        val timetableHtml = resource.payload
        val table = timetableHtml?.let(HtmlParser::extractTables)?.firstOrNull()

        logger.d(TAG, "getTimetableFromApi table: $table")

        val classes = table?.rows?.mapNotNull { row ->
            logger.d(TAG, "getTimetableFromApi row: $row")
            val dayCell = row.cells.getOrNull(DAY_INDEX) ?: return@mapNotNull null
            val intervalCell = row.cells.getOrNull(INTERVAL_INDEX) ?: return@mapNotNull null
            val frequencyCell = row.cells.getOrNull(FREQUENCY_INDEX) ?: return@mapNotNull null
            val studyLineCell = row.cells.getOrNull(STUDY_LINE_INDEX) ?: return@mapNotNull null
            val participantCell = row.cells.getOrNull(PARTICIPANT_INDEX) ?: return@mapNotNull null
            val classTypeCell = row.cells.getOrNull(CLASS_TYPE_INDEX) ?: return@mapNotNull null
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
                classTypeCell.value,
                subjectCell.value,
                teacherCell.value,
            ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

            TimetableClass(
                id = id,
                day = dayCell.value,
                startHour = startHour,
                endHour = endHour,
                frequencyId = frequencyCell.value,
                room = room.name,
                field = studyLineCell.value,
                participant = participantCell.value,
                classType = classTypeCell.value,
                ownerId = room.id,
                subject = subjectCell.value,
                teacher = teacherCell.value,
                isVisible = true,
                configurationId = configurationId
            )
        }

        logger.d(TAG, "getTimetableFromApi classes: $classes")

        return when {
            classes == null -> Resource(null, resource.status)
            else -> Resource(Timetable(room, classes), Status.Success)
        }
    }

    /**
     * Sorts rooms by name
     */
    private fun sortRooms(
        rooms: List<Owner.Room>,
    ): List<Owner.Room> {
        return rooms.sortedBy { it.name }
    }

    /**
     * Maps a [Owner.Room] to a [RoomEntity]
     */
    private fun mapRoomToEntity(owner: Owner.Room): RoomEntity {
        return RoomEntity(
            id = owner.id,
            name = owner.name,
            location = owner.location,
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
            location = entity.location,
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
        private const val CLASS_TYPE_INDEX = 5
        private const val SUBJECT_INDEX = 6
        private const val TEACHER_INDEX = 7

        // Interval indexes
        private const val START_HOUR_INDEX = 0
        private const val END_HOUR_INDEX = 1
    }
}
