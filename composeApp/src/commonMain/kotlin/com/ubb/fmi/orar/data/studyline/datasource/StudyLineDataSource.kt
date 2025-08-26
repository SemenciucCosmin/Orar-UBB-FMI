package com.ubb.fmi.orar.data.studyline.datasource

import com.ubb.fmi.orar.data.studyline.model.StudyLine
import com.ubb.fmi.orar.data.studyline.model.StudyLineTimetable
import com.ubb.fmi.orar.network.model.Resource

interface StudyLineDataSource {

    suspend fun getStudyLines(
        year: Int,
        semesterId: String
    ): Resource<List<StudyLine>>

    suspend fun getStudyGroupsIds(
        year: Int,
        semesterId: String,
        studyLineId: String
    ): Resource<List<String>>

    suspend fun getTimetables(
        year: Int,
        semesterId: String
    ): Resource<List<StudyLineTimetable>>

    suspend fun getTimetable(
        year: Int,
        semesterId: String,
        studyLineId: String
    ): Resource<StudyLineTimetable>

    suspend fun changeTimetableClassVisibility(
        timetableClassId: String
    )
}