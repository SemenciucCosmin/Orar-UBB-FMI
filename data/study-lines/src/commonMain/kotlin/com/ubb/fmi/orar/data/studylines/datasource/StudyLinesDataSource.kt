package com.ubb.fmi.orar.data.studylines.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.model.TimetableOwner

interface StudyLinesDataSource {

    suspend fun getOwners(
        year: Int,
        semesterId: String
    ): Resource<List<TimetableOwner.StudyLine>>

    suspend fun getGroups(
        year: Int,
        semesterId: String,
        ownerId: String
    ): Resource<List<String>>

    suspend fun getTimetable(
        year: Int,
        semesterId: String,
        ownerId: String,
        groupId: String,
    ): Resource<Timetable<TimetableOwner.StudyLine>>

    suspend fun changeTimetableClassVisibility(
        timetableClassId: String
    )

    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}
