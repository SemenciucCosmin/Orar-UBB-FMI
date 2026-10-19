package com.ubb.fmi.orar.data.subjects.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.Timetable
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing data flow and data source for subjects
 */
interface SubjectsRepository {

    /**
     * Retrieves a [Flow] of subjects
     */
    fun getSubjects(): Flow<Resource<List<Owner.Subject>>>

    /**
     * Retrieves a [Flow] of timetable for certain subjects
     */
    fun getTimetable(subjectId: String): Flow<Resource<Timetable<Owner.Subject>>>

    /**
     * Fetches the subjects from API again, to recover from a failed fetch
     */
    fun refresh()

    /**
     * Fetches the timetable for certain subject from API again, to recover from a failed fetch
     */
    fun refreshTimetable(subjectId: String)

    /**
     * Invalidates subjects cache
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}