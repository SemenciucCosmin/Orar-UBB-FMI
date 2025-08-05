package com.ubb.fmi.orar.data.teachers.datasource

import com.ubb.fmi.orar.data.teachers.model.TeacherTimetable
import com.ubb.fmi.orar.network.model.Resource

interface TeachersDataSource {

    suspend fun getTimetables(year: Int, semesterId: String): Resource<List<TeacherTimetable>>

}