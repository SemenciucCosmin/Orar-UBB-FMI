package com.ubb.fmi.orar.data.teachers.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.Owner
import kotlinx.coroutines.flow.Flow

/**
 * Data source for managing teacher related information
 */
interface TeachersDataSource {

    /**
     * Retrieves [Flow] of teachers from database
     */
    fun getTeachersFromCache(
        year: Int,
        semesterId: String,
    ): Flow<List<Owner.Teacher>>

    /**
     * Saves [teachers] in database
     */
    suspend fun saveTeachersInCache(
        teachers: List<Owner.Teacher>
    )

    /**
     * Retrieves teacher from API
     */
    suspend fun getTeachersFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<Owner.Teacher>>

    /**
     * Retrieves teacher events from API
     */
    suspend fun getEventsFromApi(
        year: Int,
        semesterId: String,
        teacher: Owner.Teacher,
    ): Resource<List<Event>>

    /**
     * Invalidates all cached teachers
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}
