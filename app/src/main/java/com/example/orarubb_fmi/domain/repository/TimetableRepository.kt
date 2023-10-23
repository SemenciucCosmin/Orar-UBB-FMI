package com.example.orarubb_fmi.domain.repository

import com.example.orarubb_fmi.model.Timetable
import com.example.orarubb_fmi.model.TimetableInfo

interface TimetableRepository {
    suspend fun getTimetables(timetableInfo: TimetableInfo): List<Timetable>

    suspend fun getCachedTimetable(): Timetable

    suspend fun saveTimetable(timetable: Timetable)
}