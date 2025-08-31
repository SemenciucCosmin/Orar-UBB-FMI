package com.ubb.fmi.orar.feature.studylinetimetable.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.groups.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.ui.catalog.model.Frequency
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.TimetableUiState
import com.ubb.fmi.orar.data.network.model.isError
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.timetable.model.StudyLevel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class StudyLineTimetableViewModel(
    private val fieldId: String,
    private val studyLevelId: String,
    private val groupId: String,
    private val studyLinesDataSource: StudyLinesDataSource,
    private val timetablePreferences: TimetablePreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimetableUiState())
    val uiState = _uiState.asStateFlow()
        .onStart { loadTimetable()}
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    private fun loadTimetable() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }

            val studyLevel = StudyLevel.getById(studyLevelId)
            val lineId = fieldId + studyLevel.notation

            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            if (configuration == null) {
                _uiState.update { it.copy(isLoading = false, isError = true) }
                return@launch
            }

            val resource = studyLinesDataSource.getTimetable(
                year = configuration.year,
                semesterId = configuration.semesterId,
                ownerId = lineId,
                groupId = groupId
            )

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = resource.status.isError(),
                    classes = resource.payload?.classes?.toImmutableList() ?: persistentListOf(),
                    title = resource.payload?.owner?.name ?: String.BLANK,
                    subtitle = "${studyLevel.label} - $groupId"
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
