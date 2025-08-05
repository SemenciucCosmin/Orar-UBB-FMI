package com.ubb.fmi.orar.data.rooms.datasource

import com.ubb.fmi.orar.data.rooms.model.RoomTimetable
import com.ubb.fmi.orar.network.model.Resource

interface RoomsDataSource {

    suspend fun getTimetables(year: Int, semesterId: String): Resource<List<RoomTimetable>>
}