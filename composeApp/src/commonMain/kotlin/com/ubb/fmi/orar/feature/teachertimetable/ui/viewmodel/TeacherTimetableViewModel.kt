package com.ubb.fmi.orar.feature.teachertimetable.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.core.model.Frequency
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.feature.teachertimetable.ui.viewmodel.model.TeacherTimetableUiState
import com.ubb.fmi.orar.network.model.isError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class TeacherTimetableViewModel(
    private val teacherId: String,
    private val teachersDataSource: TeachersDataSource,
    private val timetablePreferences: TimetablePreferences
): ViewModel() {

    private val _uiState = MutableStateFlow(TeacherTimetableUiState())
    val uiState = _uiState.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    init {
        loadTimetable()
    }

    private fun loadTimetable() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }

            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            if (configuration == null) {
                _uiState.update { it.copy(isLoading = false, isError = true) }
                return@launch
            }

            val timetableResource = teachersDataSource.getTimetable(
                year = configuration.year,
                semesterId = configuration.semesterId,
                teacherId = teacherId
            )

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = timetableResource.status.isError(),
                    classes = timetableResource.payload?.classes ?: emptyList(),
                    teacher = timetableResource.payload?.teacher
                )
            }
        }
    }

    fun selectFrequency(frequency: Frequency) {
        _uiState.update { it.copy(selectedFrequency = frequency) }
    }

    fun retry() {
        loadTimetable()
    }
}