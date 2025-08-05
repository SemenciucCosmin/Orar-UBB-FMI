package com.ubb.fmi.orar.data.subjects.datasource

import com.ubb.fmi.orar.data.subjects.model.SubjectTimetable
import com.ubb.fmi.orar.network.model.Resource

interface SubjectsDataSource {

    suspend fun getTimetables(year: Int, semesterId: String): Resource<List<SubjectTimetable>>
}