package com.ubb.fmi.orar.data.teachers.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.Timetable
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing data flow and data source for teachers
 */
interface TeacherRepository {

    /**
     * Retrieves a [Flow] of teachers
     */
    fun getTeachers(): Flow<Resource<List<Owner.Teacher>>>

    /**
     * Retrieves a [Flow] of timetable for certain teacher
     */
    fun getTimetable(teacherId: String): Flow<Resource<Timetable<Owner.Teacher>>>

    /**
     * Invalidates teachers cache
     */
    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}