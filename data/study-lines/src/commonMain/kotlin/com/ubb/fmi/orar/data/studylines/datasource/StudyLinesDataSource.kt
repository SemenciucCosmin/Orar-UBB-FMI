package com.ubb.fmi.orar.data.studylines.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.model.TimetableOwner

/**
 * Data source for managing study line related information
 */
interface StudyLinesDataSource {

    /**
     * Retrieve list of [TimetableOwner.StudyLine] objects from cache or API
     * by [year] and [semesterId]
     */
    suspend fun getOwners(
        year: Int,
        semesterId: String
    ): Resource<List<TimetableOwner.StudyLine>>

    /**
     * Retrieve list of groups of a [TimetableOwner.StudyLine] from cache or API
     * by [year], [semesterId] and [ownerId]
     */
    suspend fun getGroups(
        year: Int,
        semesterId: String,
        ownerId: String
    ): Resource<List<String>>

    /**
     * Retrieve timetable of [TimetableOwner.StudyLine] for specific study line from cache or
     * API by [year], [semesterId], [ownerId] and [groupId]
     */
    suspend fun getTimetable(
        year: Int,
        semesterId: String,
        ownerId: String,
        groupId: String,
    ): Resource<Timetable<TimetableOwner.StudyLine>>

    /**
     * Change visibility of specific study line timetable class by [timetableClassId]
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
