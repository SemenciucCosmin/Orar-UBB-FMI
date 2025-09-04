package com.ubb.fmi.orar.feature.studylinetimetable.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.network.model.isError
import com.ubb.fmi.orar.data.studylines.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.ui.catalog.model.StudyLevel
import com.ubb.fmi.orar.ui.catalog.model.Frequency
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.TimetableUiState
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

/**
 * ViewModel for the Study Line Timetable feature.
 *
 * This ViewModel is responsible for loading and managing the timetable data for a specific study line,
 * including handling loading states, errors, and user interactions such as frequency selection.
 *
 * @property fieldId The ID of the field of study.
 * @property studyLevelId The ID of the study level.
 * @property groupId The ID of the group.
 * @property studyLinesDataSource The data source for fetching study lines data.
 * @property timetablePreferences Preferences related to the timetable configuration.
 */
class StudyLineTimetableViewModel(
    private val fieldId: String,
    private val studyLevelId: String,
    private val groupId: String,
    private val studyLinesDataSource: StudyLinesDataSource,
    private val timetablePreferences: TimetablePreferences
) : ViewModel() {

    /**
     * Mutable state flow representing the UI state of the timetable.
     * It holds information about loading status, error status, classes, title, study level, group,
     * and the selected frequency.
     */
    private val _uiState = MutableStateFlow(TimetableUiState())
    val uiState = _uiState.asStateFlow()
        .onStart { loadTimetable() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    /**
     * Initializes the ViewModel and starts loading the timetable data.
     * The timetable is loaded asynchronously, and the UI state is updated accordingly.
     */
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
                    studyLevel = studyLevel,
                    group = groupId,
                )
            }
        }
    }

    /**
     * Selects a frequency for the timetable.
     * This updates the UI state with the selected frequency.
     *
     * @param frequency The frequency to be selected.
     */
    fun selectFrequency(frequency: Frequency) {
        _uiState.update { it.copy(selectedFrequency = frequency) }
    }

    /**
     * Retries loading the timetable data.
     * This is typically called when an error occurs and the user wants to try loading the data again.
     */
    fun retry() {
        loadTimetable()
    }
}
