package com.ubb.fmi.orar.data.rooms.datasource

import com.ubb.fmi.orar.data.database.dao.RoomClassDao
import com.ubb.fmi.orar.data.database.dao.RoomDao
import com.ubb.fmi.orar.data.database.model.RoomClassEntity
import com.ubb.fmi.orar.data.database.model.RoomEntity
import com.ubb.fmi.orar.data.rooms.api.RoomsApi
import com.ubb.fmi.orar.data.rooms.model.Room
import com.ubb.fmi.orar.data.rooms.model.RoomTimetable
import com.ubb.fmi.orar.data.rooms.model.RoomClass
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.Status
import com.ubb.fmi.orar.network.model.isError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import okio.ByteString.Companion.encodeUtf8
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.orEmpty

class RoomsDataSourceImpl(
    private val roomsApi: RoomsApi,
    private val roomDao: RoomDao,
    private val roomClassDao: RoomClassDao
) : RoomsDataSource {

    override suspend fun getRooms(
        year: Int,
        semesterId: String
    ): Resource<List<Room>> {
        val cachedRooms = getRoomsFromCache()

        return when {
            cachedRooms.isNotEmpty() -> {
                println("TESTMESSAGE Rooms: from cache")
                Resource(cachedRooms, Status.Success)
            }

            else -> {
                println("TESTMESSAGE Rooms: from API")
                val apiRoomsResource = getRoomsFromApi(year, semesterId)
                apiRoomsResource.payload?.forEach { room ->
                    val roomEntity = mapRoomToEntity(room)
                    roomDao.insertRoom(roomEntity)
                }

                apiRoomsResource
            }
        }
    }

    override suspend fun getTimetables(
        year: Int,
        semesterId: String
    ): Resource<List<RoomTimetable>> {
        val cachedTimetables = getTimetablesFromCache()

        return when {
            cachedTimetables.isNotEmpty() -> {
                println("TESTMESSAGE Rooms timetable: from cache")
                Resource(cachedTimetables, Status.Success)
            }

            else -> {
                println("TESTMESSAGE Rooms timetable: from API")
                val apiTimetablesResource = getTimetablesFromApi(year, semesterId)
                apiTimetablesResource.payload?.forEach { timetable ->
                    val roomEntity = mapRoomToEntity(timetable.room)
                    val classesEntities = mapClassesToEntities(
                        roomId = timetable.room.id,
                        classes = timetable.classes
                    )

                    roomDao.insertRoom(roomEntity)
                    classesEntities.forEach { roomClassDao.insertRoomClass(it) }
                }

                apiTimetablesResource
            }
        }
    }

    private suspend fun getRoomsFromCache(): List<Room> {
        val entities = roomDao.getRooms()
        return entities.map(::mapEntityToRoom)
    }

    private suspend fun getTimetablesFromCache(): List<RoomTimetable> {
        val roomEntities = roomDao.getRooms()
        val roomClassEntities = roomClassDao.getRoomsClasses()
        val groupedRoomClassEntities = roomClassEntities.groupBy { it.roomId }
        val roomWithClassesEntities = roomEntities.associateWith { roomEntity ->
            groupedRoomClassEntities[roomEntity.id].orEmpty()
        }.filter { it.value.isNotEmpty() }

        return roomWithClassesEntities.map { (roomEntity, classesEntities) ->
            RoomTimetable(
                room = mapEntityToRoom(roomEntity),
                classes = mapEntitiesToClasses(classesEntities),
            )
        }
    }

    private suspend fun getTimetablesFromApi(
        year: Int,
        semesterId: String
    ): Resource<List<RoomTimetable>> {
        return withContext(Dispatchers.Default) {
            val roomsResource = getRoomsFromApi(year, semesterId)
            val rooms = roomsResource.payload ?: emptyList()
            val roomTimetablesResources = rooms.map { room ->
                async { getRoomTimetableFromApi(year, semesterId, room) }
            }.awaitAll()

            val roomTimetables = roomTimetablesResources.mapNotNull { it.payload }
            val errorStatus = roomTimetablesResources.map { it.status }.firstOrNull { it.isError() }

            return@withContext when {
                roomsResource.status.isError() -> Resource(null, roomsResource.status)
                errorStatus != null -> Resource(null, errorStatus)
                else -> Resource(roomTimetables, Status.Success)
            }
        }
    }

    private suspend fun getRoomTimetableFromApi(
        year: Int,
        semesterId: String,
        room: Room
    ): Resource<RoomTimetable> {
        val resource = roomsApi.getRoomTimetableHtml(
            year = year,
            semesterId = semesterId,
            roomId = room.id
        )

        val roomTimetableHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val roomTable = HtmlParser.extractTables(
            html = roomTimetableHtml
        ).firstOrNull()

        val classes = roomTable?.rows?.mapNotNull { row ->
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

            val participantId = when {
                participantCell.value.contains(SEMIGROUP_1_ID) -> SEMIGROUP_1_ID
                participantCell.value.contains(SEMIGROUP_2_ID) -> SEMIGROUP_2_ID
                participantCell.value.all { it.isDigit() } -> WHOLE_GROUP_ID
                else -> WHOLE_YEAR_ID
            }

            val id = listOf(
                dayCell.value,
                intervalCell.value,
                frequencyCell.value,
                studyLineCell.value,
                participantCell.value,
                classTypeCell.value,
                subjectCell.id,
                teacherCell.id,
            ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

            RoomClass(
                id = id,
                day = dayCell.value,
                startHour = "$startHour:00",
                endHour = "$endHour:00",
                frequencyId = frequencyCell.value,
                studyLineId = studyLineCell.value,
                participantId = participantId,
                participantName = participantCell.value,
                classTypeId = classTypeCell.value,
                subjectId = subjectCell.id,
                teacherId = teacherCell.id
            )
        }

        return when {
            classes == null -> Resource(null, Status.Error)
            else -> Resource(RoomTimetable(room, classes), Status.Success)
        }
    }

    private suspend fun getRoomsFromApi(
        year: Int,
        semesterId: String
    ): Resource<List<Room>> {
        val resource = roomsApi.getRoomsMapHtml(
            year = year,
            semesterId = semesterId
        )

        val roomsMapHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val roomsTable = HtmlParser.extractTables(
            html = roomsMapHtml
        ).firstOrNull()

        val rooms = roomsTable?.rows?.mapNotNull { row ->
            val nameCell = row.cells.getOrNull(NAME_INDEX) ?: return@mapNotNull null
            val locationCell = row.cells.getOrNull(LOCATION_INDEX) ?: return@mapNotNull null

            Room(
                id = nameCell.id,
                name = nameCell.value,
                location = locationCell.value
            )
        }

        return when {
            rooms.isNullOrEmpty() -> Resource(null, Status.Error)
            else -> Resource(rooms, Status.Success)
        }
    }

    private fun mapEntityToRoom(roomEntity: RoomEntity): Room {
        return Room(
            id = roomEntity.id,
            name = roomEntity.name,
            location = roomEntity.location,
        )
    }

    private fun mapEntitiesToClasses(entities: List<RoomClassEntity>): List<RoomClass> {
        return entities.map { roomClassEntity ->
            RoomClass(
                id = roomClassEntity.id,
                day = roomClassEntity.day,
                startHour = roomClassEntity.startHour,
                endHour = roomClassEntity.endHour,
                frequencyId = roomClassEntity.frequencyId,
                studyLineId = roomClassEntity.studyLineId,
                participantId =  roomClassEntity.participantId,
                participantName =  roomClassEntity.participantName,
                classTypeId = roomClassEntity.classTypeId,
                subjectId = roomClassEntity.subjectId,
                teacherId = roomClassEntity.teacherId
            )
        }
    }

    private fun mapRoomToEntity(room: Room): RoomEntity {
        return RoomEntity(
            id = room.id,
            name = room.name,
            location = room.location
        )
    }

    private fun mapClassesToEntities(
        roomId: String,
        classes: List<RoomClass>
    ): List<RoomClassEntity> {
        return classes.map { roomClass ->
            RoomClassEntity(
                id = roomClass.id,
                roomId = roomId,
                day = roomClass.day,
                startHour = roomClass.startHour,
                endHour = roomClass.endHour,
                frequencyId = roomClass.frequencyId,
                studyLineId = roomClass.studyLineId,
                participantId =  roomClass.participantId,
                participantName =  roomClass.participantName,
                classTypeId = roomClass.classTypeId,
                subjectId = roomClass.subjectId,
                teacherId = roomClass.teacherId
            )
        }
    }

    companion object {
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

        // ParticipantIds
        private const val SEMIGROUP_1_ID = "/1"
        private const val SEMIGROUP_2_ID = "/2"
        private const val WHOLE_GROUP_ID = "whole_group"
        private const val WHOLE_YEAR_ID = "whole_year"
    }
}
