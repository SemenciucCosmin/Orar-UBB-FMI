package com.ubb.fmi.orar.data.subjects.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.Owner
import kotlinx.coroutines.flow.Flow

/**
 * Data source for managing subject related information
 */
interface SubjectsDataSource {

    fun getSubjectsFromCache(
        year: Int,
        semesterId: String,
    ): Flow<List<Owner.Subject>>

    suspend fun saveSubjectsInCache(
        subjects: List<Owner.Subject>
    )

    suspend fun getSubjectsFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<Owner.Subject>>

    suspend fun getEventsFromApi(
        year: Int,
        semesterId: String,
        subject: Owner.Subject,
    ): Resource<List<Event>>

    /**
     * Invalidates all cached subjects by [year] and [semesterId]
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}
