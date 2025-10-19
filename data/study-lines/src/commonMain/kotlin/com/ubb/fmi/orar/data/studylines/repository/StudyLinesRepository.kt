package com.ubb.fmi.orar.data.studylines.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.StudyLine
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing data flow and data source for study lines
 */
interface StudyLinesRepository {

    /**
     * Retrieves a [Flow] of study lines
     */
    fun getStudyLines(): Flow<Resource<List<StudyLine>>>

    /**
     * Invalidates study lines cache
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}