package com.ubb.fmi.orar.data.students.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.StudyLine
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.model.Owner

/**
 * Data source for managing students related information
 */
interface GroupsDataSource {

    /**
     * Retrieve list of [Owner.Group] from cache or API
     * by [year], [semesterId] and [studyLineId]
     */
    suspend fun getGroups(
        year: Int,
        semesterId: String,
        studyLineId: String
    ): Resource<List<Owner.Group>>

    /**
     * Retrieve timetable of [Owner.Group] for specific group from cache or
     * API by [year], [semesterId] and [groupId]
     */
    suspend fun getTimetable(
        year: Int,
        semesterId: String,
        groupId: String,
        studyLineId: String
    ): Resource<Timetable<Owner.Group>>

    /**
     * Invalidates all cached studyLines and groups by [year] and [semesterId]
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}
