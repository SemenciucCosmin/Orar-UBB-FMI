package com.ubb.fmi.orar.data.students.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.StudyLine
import kotlinx.coroutines.flow.Flow

/**
 * Data source for managing study line related information
 */
interface StudyLinesDataSource {

    fun getStudyLinesFromCache(
        year: Int,
        semesterId: String,
    ): Flow<List<StudyLine>>

    suspend fun saveStudyLinesInCache(
        studyLines: List<StudyLine>,
    )

    suspend fun getStudyLinesFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<StudyLine>>

    /**
     * Invalidates all cached study lines by [year] and [semesterId]
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}
