package com.example.orarubb_fmi.domain.repository

import com.example.orarubb_fmi.model.Timetable
import com.example.orarubb_fmi.model.TimetableInfo

interface TimetableRepository {
    suspend fun getGroups(timetableInfo: TimetableInfo): List<String>

    suspend fun getTimetable(timetableInfo: TimetableInfo): Timetable

    suspend fun getCachedTimetable(): Timetable

    suspend fun saveTimetable(timetable: Timetable)
}
