package com.ubb.fmi.orar.data.teachers.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.Timetable

/**
 * Data source for managing teacher related information
 */
interface TeachersDataSource {

    /**
     * Retrieve list of [Owner.Teacher] objects from cache or API
     * by [year] and [semesterId]
     */
    suspend fun getTeachers(
        year: Int,
        semesterId: String
    ): Resource<List<Owner.Teacher>>

    /**
     * Retrieve timetable of [Owner.Teacher] for specific teacher from cache or
     * API by [year], [semesterId] and [teacherId]
     */
    suspend fun getTimetable(
        year: Int,
        semesterId: String,
        teacherId: String,
    ): Resource<Timetable<Owner.Teacher>>

    /**
     * Invalidates all cached teachers by [year] and [semesterId]
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}
