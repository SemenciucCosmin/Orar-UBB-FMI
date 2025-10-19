package com.ubb.fmi.orar.data.groups.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.Timetable
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing data flow and data source for groups
 */
interface GroupsRepository {

    /**
     * Retrieves a [Flow] of groups
     */
    fun getGroups(studyLineId: String): Flow<Resource<List<Owner.Group>>>

    /**
     * Retrieves a [Flow] of timetable for certain group
     */
    fun getTimetable(groupId: String, studyLineId: String): Flow<Resource<Timetable<Owner.Group>>>

    /**
     * Invalidates groups cache
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}