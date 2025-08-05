package com.ubb.fmi.orar.data.studyline.datasource

import com.ubb.fmi.orar.data.studyline.model.StudyLineTimetable
import com.ubb.fmi.orar.network.model.Resource

interface StudyLineDataSource {

    suspend fun getTimetables(year: Int, semesterId: String): Resource<List<StudyLineTimetable>>
}