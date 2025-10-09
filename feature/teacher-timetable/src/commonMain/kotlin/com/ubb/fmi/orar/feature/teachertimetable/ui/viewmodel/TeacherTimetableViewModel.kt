package com.ubb.fmi.orar.feature.teachertimetable.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.usertimetable.model.Week
import com.ubb.fmi.orar.domain.usertimetable.usecase.GetCurrentWeekUseCase
import com.ubb.fmi.orar.ui.catalog.extensions.toErrorStatus
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus
import com.ubb.fmi.orar.ui.catalog.model.Frequency
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.TimetableUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * ViewModel for managing the teacher's timetable.
 *
 * This ViewModel fetches the timetable data for a specific teacher and manages the UI state
 * related to the timetable, including loading states, error handling, and frequency selection.
 *
 * @property teacherId The ID of the teacher whose timetable is being managed.
 * @property teachersDataSource The data source for fetching teacher-related data.
 * @property timetablePreferences Preferences related to the timetable configuration.
 */
class TeacherTimetableViewModel(
    private val teacherId: String,
    private val teachersDataSource: TeachersDataSource,
    private val timetablePreferences: TimetablePreferences,
    private val getCurrentWeekUseCase: GetCurrentWeekUseCase,
    private val logger: Logger,
) : ViewModel() {

    /**
     * Mutable state flow representing the UI state of the timetable.
     * It is updated with loading states, error states, and fetched classes.
     */
    private val _uiState = MutableStateFlow(TimetableUiState())
    val uiState = _uiState.asStateFlow()
        .onStart {
            getWeek()
            loadTimetable()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    /**
     * Initializes the ViewModel and starts loading the timetable.
     * This function is called when the ViewModel is created.
     * It fetches the timetable configuration and loads the timetable data.
     */
    private fun loadTimetable() {
        viewModelScope.launch {
            logger.d(TAG, "loadTimetable teacher: $teacherId")
            _uiState.update { it.copy(isLoading = true, errorStatus = null) }

            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            logger.d(TAG, "loadTimetable configuration: $configuration")

            if (configuration == null) {
                _uiState.update { it.copy(isLoading = false, errorStatus = ErrorStatus.NOT_FOUND) }
                return@launch
            }

            val timetableResource = teachersDataSource.getTimetable(
                year = configuration.year,
                semesterId = configuration.semesterId,
                teacherId = teacherId,
            )

            val teachersResource = teachersDataSource.getTeachers(
                year = configuration.year,
                semesterId = configuration.semesterId,
            )

            logger.d(TAG, "loadTimetable resource: $timetableResource")
            val teacher = teachersResource.payload?.firstOrNull { it.id == teacherId }
            val classes = timetableResource.payload?.classes?.map {
                it.copy(isVisible = true)
            }?.toImmutableList() ?: persistentListOf()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorStatus = timetableResource.status.toErrorStatus(),
                    classes = classes,
                    title = teacher?.name ?: String.BLANK
                )
            }
        }
    }

    /**
     * Retrieves the current week for proper timetable filtering
     */
    private fun getWeek() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorStatus = null) }
        getCurrentWeekUseCase().collectLatest { week ->
            val frequency = when (week) {
                Week.ODD -> Frequency.WEEK_1
                Week.EVEN -> Frequency.WEEK_2
            }

            selectFrequency(frequency)
        }
    }

    /**
     * Selects a frequency for the timetable.
     * This function updates the UI state with the selected frequency.
     *
     * @param frequency The frequency to be selected.
     */
    fun selectFrequency(frequency: Frequency) {
        logger.d(TAG, "selectFrequency: $frequency")
        _uiState.update { it.copy(selectedFrequency = frequency) }
    }

    /**
     * Retries loading the timetable.
     * This function is called when the user wants to retry fetching the timetable data,
     * typically after an error has occurred.
     */
    fun retry() {
        logger.d(TAG, "retry")
        loadTimetable()
    }

    companion object {
        private const val TAG = "TeacherTimetableViewModel"
    }
}