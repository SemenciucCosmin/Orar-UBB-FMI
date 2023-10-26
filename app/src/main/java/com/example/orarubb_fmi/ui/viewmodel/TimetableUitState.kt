package com.example.orarubb_fmi.ui.viewmodel

import com.example.orarubb_fmi.domain.model.RequestStatus
import com.example.orarubb_fmi.model.Timetable

data class TimetableUitState(
    val groups: List<String> = emptyList(),
    val timetable: Timetable? = null,
    val requestStatus: RequestStatus = RequestStatus.LOADING
)
