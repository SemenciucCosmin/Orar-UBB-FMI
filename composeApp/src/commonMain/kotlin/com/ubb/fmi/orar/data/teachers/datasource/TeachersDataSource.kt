package com.ubb.fmi.orar.data.teachers.datasource

import com.ubb.fmi.orar.data.teachers.model.Teacher
import com.ubb.fmi.orar.data.teachers.model.TeacherTimetable
import com.ubb.fmi.orar.network.model.Resource

interface TeachersDataSource {

    suspend fun getTeachers(year: Int, semesterId: String): Resource<List<Teacher>>

    suspend fun getTeacherTimetable(
        year: Int,
        semesterId: String,
        teacher: Teacher
    ): Resource<TeacherTimetable>
}