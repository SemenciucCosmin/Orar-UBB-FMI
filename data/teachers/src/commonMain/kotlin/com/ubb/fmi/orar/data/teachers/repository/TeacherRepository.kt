package com.ubb.fmi.orar.data.teachers.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.Timetable
import kotlinx.coroutines.flow.Flow

interface TeacherRepository {

    fun getTeachers(): Flow<Resource<List<Owner.Teacher>>>

    fun getTimetable(teacherId: String): Flow<Resource<Timetable<Owner.Teacher>>>

    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}