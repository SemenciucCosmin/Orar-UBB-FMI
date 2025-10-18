package com.ubb.fmi.orar.data.students.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.StudyLine
import com.ubb.fmi.orar.data.timetable.model.Timetable
import kotlinx.coroutines.flow.Flow

interface StudyLinesRepository {

    fun getStudyLines(): Flow<Resource<List<StudyLine>>>

    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}