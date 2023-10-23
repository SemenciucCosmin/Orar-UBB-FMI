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
            val receivedGroups = timetableRepository.getGroups(timetableInfo)
            _groups.update { receivedGroups }
        }
    }

    fun getTimetable(timetableInfo: TimetableInfo, group: String) {
        viewModelScope.launch {
            val timetable = timetableRepository.getTimetable(timetableInfo, group)
            _timetable.update { timetable }
        }
    }

    fun getCachedTimetable(): Timetable {
        TODO("Not yet implemented")
    }

    fun saveTimetable(timetable: Timetable) {
        TODO("Not yet implemented")
    }
}
