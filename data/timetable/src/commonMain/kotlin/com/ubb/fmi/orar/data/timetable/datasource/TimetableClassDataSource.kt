package com.ubb.fmi.orar.data.timetable.datasource

import com.ubb.fmi.orar.data.timetable.model.TimetableClass

/**
 * Data source for managing all timetable classes
 */
interface TimetableClassDataSource {

    /**
     * Retrieve list of [TimetableClass] for specific [ownerId] from cache
     * by [configurationId]
     */
    suspend fun getTimetableClassesFromCache(
        configurationId: String,
        ownerId: String,
    ): List<TimetableClass>

    /**
     * Saves new list of [TimetableClass] to cache
     */
    suspend fun saveTimetableClassesInCache(classes: List<TimetableClass>)

    /**
     * Sorts [TimetableClass] objects by day order index, start hour and end hour
     */
    fun sortTimetableClasses(classes: List<TimetableClass>): List<TimetableClass>

    /**
     * Change visibility of specific timetable class by [timetableClassId]
     */
    suspend fun changeTimetableClassVisibility(timetableClassId: String)

    /**
     * Invalidates all cached data for by [year] and [semesterId]
     */
    suspend fun invalidate(year: Int, semesterId: String)
}
