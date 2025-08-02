package com.ubb.fmi.orar.data.rooms.datasource

import com.ubb.fmi.orar.data.model.Semester
import com.ubb.fmi.orar.data.rooms.model.Room
import com.ubb.fmi.orar.data.rooms.model.RoomTimetable
import com.ubb.fmi.orar.network.model.Resource

interface RoomsDataSource {

    suspend fun getRooms(year: Int, semester: Semester): Resource<List<Room>>

    suspend fun getRoomTimetable(
        year: Int,
        semester: Semester,
        room: Room
    ): Resource<RoomTimetable>
}