package com.ubb.fmi.orar.data.subjects.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.Timetable
import kotlinx.coroutines.flow.Flow

interface SubjectsRepository {

    fun getSubjects(): Flow<Resource<List<Owner.Subject>>>

    fun getTimetable(subjectId: String): Flow<Resource<Timetable<Owner.Subject>>>

    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}