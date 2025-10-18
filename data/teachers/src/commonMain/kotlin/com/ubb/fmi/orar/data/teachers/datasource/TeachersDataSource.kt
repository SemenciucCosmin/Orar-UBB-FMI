package com.ubb.fmi.orar.data.teachers.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.Timetable
import kotlinx.coroutines.flow.Flow

/**
 * Data source for managing teacher related information
 */
interface TeachersDataSource {

    fun getTeachersFromCache(
        year: Int,
        semesterId: String,
    ): Flow<List<Owner.Teacher>>

    suspend fun saveTeachersInCache(
        teachers: List<Owner.Teacher>
    )

    suspend fun getTeachersFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<Owner.Teacher>>

    suspend fun getEventsFromApi(
        year: Int,
        semesterId: String,
        teacher: Owner.Teacher,
    ): Resource<List<Event>>

    /**
     * Invalidates all cached teachers by [year] and [semesterId]
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}
