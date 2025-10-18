package com.ubb.fmi.orar.data.rooms.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.Timetable
import kotlinx.coroutines.flow.Flow

interface RoomsRepository {

    fun getRooms(): Flow<Resource<List<Owner.Room>>>

    fun getTimetable(roomId: String): Flow<Resource<Timetable<Owner.Room>>>

    /**
     * Invalidates all cached rooms by [year] and [semesterId]
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}