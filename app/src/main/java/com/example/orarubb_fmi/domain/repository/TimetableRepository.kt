package com.example.orarubb_fmi.domain.repository

import com.example.orarubb_fmi.model.Resource
import com.example.orarubb_fmi.model.Timetable
import com.example.orarubb_fmi.model.TimetableInfo
import okhttp3.ResponseBody

interface TimetableRepository {
    suspend fun getTimetable(timetableInfo: TimetableInfo): Resource<Timetable>
}
