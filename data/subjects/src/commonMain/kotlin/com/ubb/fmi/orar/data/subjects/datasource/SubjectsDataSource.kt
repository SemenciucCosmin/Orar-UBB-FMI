package com.ubb.fmi.orar.data.subjects.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.Owner
import kotlinx.coroutines.flow.Flow

/**
 * Data source for managing subject related information
 */
interface SubjectsDataSource {

    /**
     * Retrieves [Flow] of subjects from database
     */
    fun getSubjectsFromCache(
        year: Int,
        semesterId: String,
    ): Flow<List<Owner.Subject>>

    /**
     * Saves [subjects] in database
     */
    suspend fun saveSubjectsInCache(
        subjects: List<Owner.Subject>
    )

    /**
     * Retrieves subjects from API
     */
    suspend fun getSubjectsFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<Owner.Subject>>

    /**
     * Retrieves subjects events from API
     */
    suspend fun getEventsFromApi(
        year: Int,
        semesterId: String,
        subject: Owner.Subject,
    ): Resource<List<Event>>

    /**
     * Invalidates all cached subjects
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}
