package com.ubb.fmi.orar.data.rooms.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Event
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
     * Retrieves a [Flow] of all rooms and all their events
     */
    suspend fun getAllRoomsAndEvents(): Flow<Pair<List<Owner.Room>, List<Event>>>

    /**
     * Fetches the rooms from API again, to recover from a failed fetch
     */
    fun refresh()

    /**
     * Fetches the timetable for certain room from API again, to recover from a failed fetch
     */
    fun refreshTimetable(roomId: String)

    /**
     * Invalidates rooms cache
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}