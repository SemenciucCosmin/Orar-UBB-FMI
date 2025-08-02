package com.ubb.fmi.orar.data.teachers.datasource

import com.ubb.fmi.orar.data.model.Semester
import com.ubb.fmi.orar.data.teachers.model.Teacher
import com.ubb.fmi.orar.data.teachers.model.TeacherTimetable
import com.ubb.fmi.orar.network.model.Resource

interface TeachersDataSource {

    suspend fun getTeachers(year: Int, semester: Semester): Resource<List<Teacher>>

    suspend fun getTeacherTimetable(
        year: Int,
        semester: Semester,
        teacher: Teacher
    ): Resource<TeacherTimetable>
}