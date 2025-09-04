package com.ubb.fmi.orar.data.teachers.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.model.TimetableOwner

/**
 * Data source for managing teacher related information
 */
interface TeachersDataSource {

    /**
     * Retrieve list of [TimetableOwner.Teacher] objects from cache or API
     * by [year] and [semesterId]
     */
    suspend fun getOwners(
        year: Int,
        semesterId: String
    ): Resource<List<TimetableOwner.Teacher>>

    /**
     * Retrieve timetable of [TimetableOwner.Teacher] for specific teacher from cache or
     * API by [year], [semesterId] and [ownerId]
     */
    suspend fun getTimetable(
        year: Int,
        semesterId: String,
        ownerId: String,
    ): Resource<Timetable<TimetableOwner.Teacher>>

    /**
     * Change visibility of specific teacher timetable class by [timetableClassId]
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
