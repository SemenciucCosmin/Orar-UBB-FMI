package com.ubb.fmi.orar.feature.subjectstimetable.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.network.model.isError
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.extensions.BLANK
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
 * ViewModel for managing the state of the Subject Timetable screen.
 *
 * @property subjectId The ID of the subject for which the timetable is displayed.
 * @property subjectsDataSource The data source for fetching subject-related data.
 * @property timetablePreferences Preferences related to the timetable configuration.
 */
class SubjectTimetableViewModel(
    private val subjectId: String,
    private val subjectsDataSource: SubjectsDataSource,
    private val timetablePreferences: TimetablePreferences,
    private val logger: Logger,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimetableUiState())
    val uiState = _uiState.asStateFlow()
        .onStart { loadTimetable() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    private fun loadTimetable() {
        viewModelScope.launch {
            logger.d(TAG, "loadTimetable subjectId: $subjectId")
            _uiState.update { it.copy(isLoading = true, isError = false) }

            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            logger.d(TAG, "loadTimetable configuration: $configuration")

            if (configuration == null) {
                _uiState.update { it.copy(isLoading = false, isError = true) }
                return@launch
            }

            val resource = subjectsDataSource.getTimetable(
                year = configuration.year,
                semesterId = configuration.semesterId,
                ownerId = subjectId,
            )

            logger.d(TAG, "loadTimetable resource: $resource")

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = resource.status.isError(),
                    classes = resource.payload?.classes?.toImmutableList() ?: persistentListOf(),
                    title = resource.payload?.owner?.name ?: String.BLANK
                )
            }
        }
    }

    fun selectFrequency(frequency: Frequency) {
        logger.d(TAG, "selectFrequency: $frequency")
        _uiState.update { it.copy(selectedFrequency = frequency) }
    }

    fun retry() {
        logger.d(TAG, "retry")
        loadTimetable()
    }

    companion object {
        private const val TAG = "SubjectTimetableViewModel"
    }
}
