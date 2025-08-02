package com.ubb.fmi.orar.data.rooms.datasource

import com.ubb.fmi.orar.data.model.Semester
import com.ubb.fmi.orar.data.rooms.api.RoomsApi
import com.ubb.fmi.orar.data.rooms.model.Room
import com.ubb.fmi.orar.data.rooms.model.RoomTimetable
import com.ubb.fmi.orar.data.rooms.model.RoomTimetableClass
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.Status

class RoomsDataSourceImpl(
    private val roomsApi: RoomsApi
) : RoomsDataSource {

    override suspend fun getRooms(year: Int, semester: Semester): Resource<List<Room>> {
        val resource = roomsApi.getRoomsMapHtml(
            year = year,
            semester = semester.id
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

    override suspend fun getRoomTimetable(
        year: Int,
        semester: Semester,
        room: Room
    ): Resource<RoomTimetable> {
        val resource = roomsApi.getRoomTimetableHtml(
            year = year,
            semester = semester.id,
            roomId = room.id
        )

        val roomTimetableHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val roomTable = HtmlParser.extractTables(
            html = roomTimetableHtml
        ).firstOrNull()

        val classes = roomTable?.rows?.mapNotNull { row ->
            val dayCell = row.cells.getOrNull(DAY_INDEX) ?: return@mapNotNull null
            val hoursCell = row.cells.getOrNull(HOURS_INDEX) ?: return@mapNotNull null
            val frequencyCell = row.cells.getOrNull(FREQUENCY_INDEX) ?: return@mapNotNull null
            val studyLineCell = row.cells.getOrNull(STUDY_LINE_INDEX) ?: return@mapNotNull null
            val classTypeCell = row.cells.getOrNull(CLASS_TYPE_INDEX) ?: return@mapNotNull null
            val subjectCell = row.cells.getOrNull(SUBJECT_INDEX) ?: return@mapNotNull null
            val teacherCell = row.cells.getOrNull(TEACHER_INDEX) ?: return@mapNotNull null

            RoomTimetableClass(
                day = dayCell.value,
                hours = hoursCell.value,
                frequencyId = frequencyCell.value,
                studyLineId = studyLineCell.value,
                classTypeId = classTypeCell.value,
                subjectId = subjectCell.id,
                teacherId = teacherCell.id
            )
        }

        return when {
            classes.isNullOrEmpty() -> Resource(null, Status.Error)
            else -> Resource(RoomTimetable(room, classes), Status.Success)
        }
    }

    companion object {
        // Rooms map column indexes
        private const val NAME_INDEX = 0
        private const val LOCATION_INDEX = 1

        // Room timetable column indexes
        private const val DAY_INDEX = 0
        private const val HOURS_INDEX = 1
        private const val FREQUENCY_INDEX = 2
        private const val STUDY_LINE_INDEX = 4
        private const val CLASS_TYPE_INDEX = 5
        private const val SUBJECT_INDEX = 6
        private const val TEACHER_INDEX = 7
    }
}
