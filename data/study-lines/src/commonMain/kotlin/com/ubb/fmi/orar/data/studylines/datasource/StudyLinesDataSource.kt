package com.ubb.fmi.orar.data.studylines.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.StudyLine
import kotlinx.coroutines.flow.Flow

/**
 * Data source for managing study line related information
 */
interface StudyLinesDataSource {

    /**
     * Retrieves [Flow] of study lines from database
     */
    fun getStudyLinesFromCache(
        year: Int,
        semesterId: String,
    ): Flow<List<StudyLine>>

    /**
     * Saves [studyLines] in database
     */
    suspend fun saveStudyLinesInCache(
        studyLines: List<StudyLine>,
    )

    /**
     * Retrieves study lines from API
     */
    suspend fun getStudyLinesFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<StudyLine>>

    /**
     * Invalidates all cached study lines
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}
