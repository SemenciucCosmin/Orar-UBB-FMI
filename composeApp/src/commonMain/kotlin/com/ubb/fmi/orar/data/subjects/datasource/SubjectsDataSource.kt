package com.ubb.fmi.orar.data.subjects.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.model.TimetableOwner

interface SubjectsDataSource {

    suspend fun getOwners(
        year: Int,
        semesterId: String
    ): Resource<List<TimetableOwner.Subject>>

    suspend fun getTimetable(
        year: Int,
        semesterId: String,
        ownerId: String,
    ): Resource<Timetable<TimetableOwner.Subject>>

    suspend fun changeTimetableClassVisibility(
        timetableClassId: String
    )

    suspend fun invalidate(
        year: Int,
        semesterId: String,
    )
}
