package com.ubb.fmi.orar.data.subjects.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.model.TimetableOwner

/**
 * Data source for managing subject related information
 */
interface SubjectsDataSource {

    /**
     * Retrieve list of [TimetableOwner.Subject] objects from cache or API
     * by [year] and [semesterId]
     */
    suspend fun getOwners(
        year: Int,
        semesterId: String
    ): Resource<List<TimetableOwner.Subject>>

    /**
     * Retrieve timetable of [TimetableOwner.Subject] for specific subject from cache or
     * API by [year], [semesterId] and [ownerId]
     */
    suspend fun getTimetable(
        year: Int,
        semesterId: String,
        ownerId: String,
    ): Resource<Timetable<TimetableOwner.Subject>>

    /**
     * Change visibility of specific subject timetable class by [timetableClassId]
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
