package com.ubb.fmi.orar.data.teachers.datasource

import com.ubb.fmi.orar.data.teachers.model.Teacher
import com.ubb.fmi.orar.data.teachers.model.TeacherTimetable
import com.ubb.fmi.orar.network.model.Resource

interface TeachersDataSource {

    suspend fun getTeachers(
        year: Int,
        semesterId: String
    ): Resource<List<Teacher>>

    suspend fun getTimetables(
        year: Int,
        semesterId: String
    ): Resource<List<TeacherTimetable>>

    suspend fun getTimetable(
        year: Int,
        semesterId: String,
        teacherId: String
    ): Resource<TeacherTimetable>
}