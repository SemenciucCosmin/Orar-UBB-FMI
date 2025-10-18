package com.ubb.fmi.orar.data.students.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.Timetable
import kotlinx.coroutines.flow.Flow

interface GroupsRepository {

    fun getGroups(studyLineId: String): Flow<Resource<List<Owner.Group>>>

    fun getTimetable(groupId: String, studyLineId: String): Flow<Resource<Timetable<Owner.Group>>>

    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}