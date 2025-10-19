package com.ubb.fmi.orar.data.groups.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.StudyLine
import kotlinx.coroutines.flow.Flow

/**
 * Data source for providing groups related information from database and API
 */
interface GroupsDataSource {

    /**
     * Retrieve groups from database as [Flow]
     */
    fun getGroupsFromCache(
        year: Int,
        semesterId: String,
        studyLine: StudyLine
    ): Flow<List<Owner.Group>>

    /**
     * Saves groups in database
     */
    suspend fun saveGroupsInCache(
        groups: List<Owner.Group>
    )

    /**
     * Retrieve groups from API
     */
    suspend fun getGroupsFromApi(
        year: Int,
        semesterId: String,
        studyLine: StudyLine
    ): Resource<List<Owner.Group>>

    /**
     * Retrieve group events from API
     */
    suspend fun getEventsFromApi(
        year: Int,
        semesterId: String,
        group: Owner.Group,
    ): Resource<List<Event>>

    /**
     * Invalidates all cached groups
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}
