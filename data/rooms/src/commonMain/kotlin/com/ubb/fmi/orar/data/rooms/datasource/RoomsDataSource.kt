package com.ubb.fmi.orar.data.rooms.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.model.TimetableOwner

/**
 * Data source for managing room related information
 */
interface RoomsDataSource {

    /**
     * Retrieve list of [TimetableOwner.Room] objects from cache or API
     * by [year] and [semesterId]
     */
    suspend fun getOwners(
        year: Int,
        semesterId: String
    ): Resource<List<TimetableOwner.Room>>

    /**
     * Retrieve timetable of [TimetableOwner.Room] for specific room from cache or
     * API by [year], [semesterId] and [ownerId]
     */
    suspend fun getTimetable(
        year: Int,
        semesterId: String,
        ownerId: String,
    ): Resource<Timetable<TimetableOwner.Room>>

    /**
     * Change visibility of specific room timetable class by [timetableClassId]
     */
    suspend fun changeTimetableClassVisibility(
        timetableClassId: String
    )

    /**
     * Invalidates all cached data for by [year] and [semesterId]
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}
