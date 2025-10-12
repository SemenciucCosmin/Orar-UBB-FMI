package com.ubb.fmi.orar.data.students.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.StudyLine

/**
 * Data source for managing study line related information
 */
interface StudyLinesDataSource {

    /**
     * Retrieve list of [StudyLine] objects from cache or API
     * by [year] and [semesterId]
     */
    suspend fun getStudyLines(
        year: Int,
        semesterId: String
    ): Resource<List<StudyLine>>

    /**
     * Invalidates all cached study lines by [year] and [semesterId]
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}
