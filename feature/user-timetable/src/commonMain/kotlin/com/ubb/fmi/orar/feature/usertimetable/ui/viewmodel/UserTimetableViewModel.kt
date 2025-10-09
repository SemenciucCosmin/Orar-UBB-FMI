package com.ubb.fmi.orar.feature.usertimetable.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.domain.timetable.usecase.ChangeTimetableClassVisibilityUseCase
import com.ubb.fmi.orar.domain.usertimetable.model.Week
import com.ubb.fmi.orar.domain.usertimetable.usecase.GetCurrentWeekUseCase
import com.ubb.fmi.orar.domain.usertimetable.usecase.GetUserTimetableUseCase
import com.ubb.fmi.orar.ui.catalog.extensions.toErrorStatus
import com.ubb.fmi.orar.ui.catalog.model.Frequency
import com.ubb.fmi.orar.ui.catalog.model.TimetableListItem
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.TimetableUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the user timetable.
 * This ViewModel handles loading the timetable data, managing UI state,
 * and providing functionality to change the visibility of timetable classes.
 * It also supports toggling edit mode and selecting frequency.
 * @property getUserTimetableUseCase Use case for fetching the user's timetable.
 * @property changeTimetableClassVisibilityUseCase Use case for changing the visibility of a timetable
 */
class UserTimetableViewModel(
    private val getUserTimetableUseCase: GetUserTimetableUseCase,
    private val changeTimetableClassVisibilityUseCase: ChangeTimetableClassVisibilityUseCase,
    private val getCurrentWeekUseCase: GetCurrentWeekUseCase,
    private val logger: Logger,
) : ViewModel() {

    /**
     * Job to manage the loading of the timetable.
     * This allows for cancellation and restarting of the loading process if needed.
     */
    private var job: Job

    /**
     * Mutable state flow to hold the UI state of the timetable.
     * This includes loading status, error status, classes, selected frequency, and edit mode.
     */
    private val _uiState = MutableStateFlow(TimetableUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Initializes the ViewModel by loading the timetable.
     * This is done in the init block to ensure it starts loading as soon as the ViewModel is created.
     */
    init {
        getWeek()
        job = loadTimetable()
    }

    /**
     * Loads the user's timetable using the provided use case.
     * It updates the UI state to reflect loading status and handles errors.
     * The timetable classes are collected and converted to an immutable list for UI consumption.
     */
    private fun loadTimetable() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorStatus = null) }
        getUserTimetableUseCase().collectLatest { resource ->
            logger.d(TAG, "loadTimetable: $resource")
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorStatus = resource.status.toErrorStatus(),
                    classes = resource.payload?.classes?.toImmutableList() ?: persistentListOf()
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
     * This updates the UI state with the newly selected frequency.
     * @param frequency The frequency to select.
     */
    fun selectFrequency(frequency: Frequency) {
        logger.d(TAG, "selectFrequency: $frequency")
        _uiState.update { it.copy(selectedFrequency = frequency) }
    }

    /**
     * Toggles the edit mode for the timetable.
     * This updates the UI state to reflect whether edit mode is currently on or off.
     */
    fun changeEditMode() {
        _uiState.update {
            logger.d(TAG, "changeEditMode to: ${!it.isEditModeOn}")
            it.copy(isEditModeOn = !it.isEditModeOn)
        }
    }

    /**
     * Changes the visibility of a specific timetable class.
     * This updates the visibility status of the class in the UI state and calls the use case to persist the change.
     * @param timetableClass The timetable class whose visibility is to be changed.
     */
    fun changeTimetableClassVisibility(timetableClass: TimetableListItem.Class) {
        viewModelScope.launch {
            logger.d(TAG, "changeTimetableClassVisibility class: $timetableClass")
            changeTimetableClassVisibilityUseCase(timetableClass.id)
        }

        _uiState.update { state ->
            val newClasses = state.classes.map {
                when {
                    it.id != timetableClass.id -> it
                    else -> it.copy(isVisible = !it.isVisible)
                }
            }.toImmutableList()

            state.copy(classes = newClasses)
        }
    }

    /**
     * Retries loading the timetable.
     * This cancels the current job and starts a new one to load the timetable again.
     */
    fun retry() {
        logger.d(TAG, "retry")
        job.cancel()
        job = loadTimetable()
    }

    companion object {
        private const val TAG = "UserTimetableViewModel"
    }
}