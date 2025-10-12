package com.ubb.fmi.orar.data.subjects.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.Timetable

/**
 * Data source for managing subject related information
 */
interface SubjectsDataSource {

    /**
     * Retrieve list of [Owner.Subject] objects from cache or API
     * by [year] and [semesterId]
     */
    suspend fun getSubjects(
        year: Int,
        semesterId: String
    ): Resource<List<Owner.Subject>>

    /**
     * Retrieve timetable of [Owner.Subject] for specific subject from cache or
     * API by [year], [semesterId] and [subjectId]
     */
    suspend fun getTimetable(
        year: Int,
        semesterId: String,
        subjectId: String,
    ): Resource<Timetable<Owner.Subject>>

    /**
     * Invalidates all cached subjects by [year] and [semesterId]
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}
