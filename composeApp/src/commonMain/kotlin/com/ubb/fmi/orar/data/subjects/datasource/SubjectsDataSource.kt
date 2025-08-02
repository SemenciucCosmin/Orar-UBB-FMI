package com.ubb.fmi.orar.data.subjects.datasource

import com.ubb.fmi.orar.data.model.Semester
import com.ubb.fmi.orar.data.rooms.model.Room
import com.ubb.fmi.orar.data.rooms.model.RoomTimetable
import com.ubb.fmi.orar.data.subjects.model.Subject
import com.ubb.fmi.orar.data.subjects.model.SubjectTimetable
import com.ubb.fmi.orar.network.model.Resource

interface SubjectsDataSource {

    suspend fun getSubjects(year: Int, semester: Semester): Resource<List<Subject>>

    suspend fun getSubjectTimetable(
        year: Int,
        semester: Semester,
        subject: Subject
    ): Resource<SubjectTimetable>
}