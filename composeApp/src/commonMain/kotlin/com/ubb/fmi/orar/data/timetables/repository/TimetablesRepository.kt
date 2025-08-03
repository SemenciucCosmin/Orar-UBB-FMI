package com.ubb.fmi.orar.data.timetables.repository

import com.ubb.fmi.orar.data.model.Semester

interface TimetablesRepository {

    suspend fun load(year: Int, semester: Semester): Long
}