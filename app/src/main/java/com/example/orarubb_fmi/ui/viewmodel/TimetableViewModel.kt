package com.example.orarubb_fmi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orarubb_fmi.domain.repository.TimetableRepository
import com.example.orarubb_fmi.model.Timetable
import com.example.orarubb_fmi.model.TimetableInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimetableViewModel(private val timetableRepository: TimetableRepository) : ViewModel() {

    private val _groups = MutableStateFlow(emptyList<String>())
    val groups = _groups.asStateFlow()

    private val _timetable = MutableStateFlow<Timetable?>(null)
    val timetable = _timetable.asStateFlow()

    fun getGroups(timetableInfo: TimetableInfo) {
        viewModelScope.launch {
            val groups = timetableRepository.getGroups(timetableInfo)
            _groups.update { groups }
        }
    }

    fun getTimetable(timetableInfo: TimetableInfo) {
        viewModelScope.launch {
            val timetable = timetableRepository.getTimetable(timetableInfo)
            _timetable.update { timetable }
        }
    }
}
