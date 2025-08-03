package com.ubb.fmi.orar.data.studyline.datasource

import com.ubb.fmi.orar.data.model.Semester
import com.ubb.fmi.orar.data.studyline.model.StudyLine
import com.ubb.fmi.orar.data.studyline.model.StudyLineTimetable
import com.ubb.fmi.orar.network.model.Resource

interface StudyLineDataSource {

    suspend fun getStudyLines(year: Int, semester: Semester): Resource<List<StudyLine>>

    suspend fun getStudyLineTimetable(
        year: Int,
        semester: Semester,
        studyLine: StudyLine
    ): Resource<StudyLineTimetable>
}