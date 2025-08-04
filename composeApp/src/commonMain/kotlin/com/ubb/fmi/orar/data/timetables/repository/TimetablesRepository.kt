package com.ubb.fmi.orar.data.timetables.repository


interface TimetablesRepository {

    suspend fun load(year: Int, semesterId: String): Long
}