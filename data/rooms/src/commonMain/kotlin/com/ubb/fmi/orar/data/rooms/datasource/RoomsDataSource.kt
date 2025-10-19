package com.ubb.fmi.orar.data.rooms.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.Owner
import kotlinx.coroutines.flow.Flow

/**
 * Data source for managing room related information
 */
interface RoomsDataSource {

    /**
     * Retrieves [Flow] of rooms from database
     */
    fun getRoomsFromCache(
        year: Int,
        semesterId: String,
    ): Flow<List<Owner.Room>>

    /**
     * Saves [rooms] in database
     */
    suspend fun saveRoomsInCache(
        rooms: List<Owner.Room>
    )

    /**
     * Retrieves rooms from API
     */
    suspend fun getRoomsFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<Owner.Room>>

    /**
     * Retrieves room events from API
     */
    suspend fun getEventsFromApi(
        year: Int,
        semesterId: String,
        room: Owner.Room,
    ): Resource<List<Event>>

    /**
     * Invalidates all cached rooms
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}
