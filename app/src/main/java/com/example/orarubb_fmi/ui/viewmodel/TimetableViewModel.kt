package com.example.orarubb_fmi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orarubb_fmi.domain.repository.TimetableRepository
import com.example.orarubb_fmi.model.TimetableInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimetableViewModel(private val timetableRepository: TimetableRepository) : ViewModel() {

    private val _timetableUiState = MutableStateFlow(TimetableUitState())
    val timetableUiState = _timetableUiState.asStateFlow()

    fun getGroups(timetableInfo: TimetableInfo) {
        viewModelScope.launch {
            val groups = timetableRepository.getGroups(timetableInfo)
            _timetableUiState.update {
                it.copy(groups = groups)
            }
        }
    }

    fun getTimetable(timetableInfo: TimetableInfo) {
        viewModelScope.launch {
            val timetable = timetableRepository.getTimetable(timetableInfo)
            _timetableUiState.update {
                it.copy(timetable = timetable)
            }
        }
    }
}
