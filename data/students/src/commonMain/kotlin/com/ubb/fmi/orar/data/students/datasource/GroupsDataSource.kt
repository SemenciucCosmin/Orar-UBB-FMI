package com.ubb.fmi.orar.data.students.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.StudyLine
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.model.Owner
import kotlinx.coroutines.flow.Flow

/**
 * Data source for managing students related information
 */
interface GroupsDataSource {

    fun getGroupsFromCache(
        year: Int,
        semesterId: String,
        studyLine: StudyLine
    ): Flow<List<Owner.Group>>

    suspend fun saveGroupsInCache(
        groups: List<Owner.Group>
    )

    suspend fun getGroupsFromApi(
        year: Int,
        semesterId: String,
        studyLine: StudyLine
    ): Resource<List<Owner.Group>>

    suspend fun getEventsFromApi(
        year: Int,
        semesterId: String,
        group: Owner.Group,
    ): Resource<List<Event>>

    /**
     * Invalidates all cached studyLines and groups by [year] and [semesterId]
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}
