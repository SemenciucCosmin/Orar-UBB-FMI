package com.ubb.fmi.orar.data.rooms.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.Timetable

/**
 * Data source for managing room related information
 */
interface RoomsDataSource {

    /**
     * Retrieve list of [Owner.Room] objects from cache or API
     * by [year] and [semesterId]
     */
    suspend fun getRooms(
        year: Int,
        semesterId: String
    ): Resource<List<Owner.Room>>

    /**
     * Retrieve timetable of [Owner.Room] for specific room from cache or
     * API by [year], [semesterId] and [roomId]
     */
    suspend fun getTimetable(
        year: Int,
        semesterId: String,
        roomId: String,
    ): Resource<Timetable<Owner.Room>>

    /**
     * Invalidates all cached rooms by [year] and [semesterId]
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}
