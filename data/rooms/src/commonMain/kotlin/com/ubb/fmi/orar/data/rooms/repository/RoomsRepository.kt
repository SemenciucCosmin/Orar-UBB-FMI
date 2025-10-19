package com.ubb.fmi.orar.data.rooms.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.Timetable
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing data flow and data source for rooms
 */
interface RoomsRepository {

    /**
     * Retrieves a [Flow] of rooms
     */
    fun getRooms(): Flow<Resource<List<Owner.Room>>>

    /**
     * Retrieves a [Flow] of timetable for certain room
     */
    fun getTimetable(roomId: String): Flow<Resource<Timetable<Owner.Room>>>

    /**
     * Invalidates rooms cache
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}