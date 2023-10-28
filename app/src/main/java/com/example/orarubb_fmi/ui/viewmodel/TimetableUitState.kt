package com.example.orarubb_fmi.ui.viewmodel

import com.example.orarubb_fmi.model.Timetable

sealed class TimetableUitState {
    data class Success(
        val groups: List<String>,
        val timetable: Timetable
    ) : TimetableUitState()

    data object Loading : TimetableUitState()

    data class Error(val message: String) : TimetableUitState()
}
