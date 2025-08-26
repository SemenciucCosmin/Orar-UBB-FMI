package com.ubb.fmi.orar.data.subjects.datasource

import com.ubb.fmi.orar.data.subjects.model.Subject
import com.ubb.fmi.orar.data.subjects.model.SubjectTimetable
import com.ubb.fmi.orar.network.model.Resource

interface SubjectsDataSource {

    suspend fun getSubjects(
        year: Int,
        semesterId: String
    ): Resource<List<Subject>>

    suspend fun getTimetables(
        year: Int,
        semesterId: String
    ): Resource<List<SubjectTimetable>>

    suspend fun getTimetable(
        year: Int,
        semesterId: String,
        subjectId: String
    ): Resource<SubjectTimetable>

    suspend fun changeTimetableClassVisibility(
        timetableClassId: String
    )
}