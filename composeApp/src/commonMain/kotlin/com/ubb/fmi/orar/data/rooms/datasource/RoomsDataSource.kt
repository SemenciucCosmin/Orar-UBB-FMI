package com.ubb.fmi.orar.data.rooms.datasource

import com.ubb.fmi.orar.data.rooms.model.Room
import com.ubb.fmi.orar.data.rooms.model.RoomTimetable
import com.ubb.fmi.orar.network.model.Resource

interface RoomsDataSource {

    suspend fun getRooms(
        year: Int,
        semesterId: String
    ): Resource<List<Room>>

    suspend fun getTimetables(
        year: Int,
        semesterId: String
    ): Resource<List<RoomTimetable>>

    suspend fun getTimetable(
        year: Int,
        semesterId: String,
        roomId: String
    ): Resource<RoomTimetable>

    suspend fun changeTimetableClassVisibility(
        timetableClassId: String
    )
}