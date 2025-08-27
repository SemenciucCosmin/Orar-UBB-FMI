package com.ubb.fmi.orar.feature.studylinetimetable.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.ui.catalog.model.Frequency
import com.ubb.fmi.orar.domain.studylines.usecase.GetStudyLineTimetableUseCase
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.TimetableUiState
import com.ubb.fmi.orar.data.network.model.isError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class StudyLineTimetableViewModel(
    private val studyLineId: String,
    private val studyGroupId: String,
    private val getStudyLineTimetableUseCase: GetStudyLineTimetableUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimetableUiState())
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
            val timetableResource = getStudyLineTimetableUseCase(studyLineId, studyGroupId)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = timetableResource.status.isError(),
                    timetable = timetableResource.payload,
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
