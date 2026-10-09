package com.ubb.fmi.orar.feature.usertimetable.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.network.model.isEmpty
import com.ubb.fmi.orar.data.network.model.isLoading
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.Week
import com.ubb.fmi.orar.domain.analytics.AnalyticsLogger
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsParameter
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsTimetableType
import com.ubb.fmi.orar.domain.notifications.usecase.ChangeEventNotificationUseCase
import com.ubb.fmi.orar.domain.timetable.usecase.ChangeEventVisibilityUseCase
import com.ubb.fmi.orar.domain.timetable.usecase.DeletePersonalEventUseCase
import com.ubb.fmi.orar.domain.usertimetable.usecase.GetCurrentWeekUseCase
import com.ubb.fmi.orar.domain.usertimetable.usecase.GetUserTimetableUseCase
import com.ubb.fmi.orar.ui.catalog.extensions.toErrorStatus
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
 * @property changeEventVisibilityUseCase Use case for changing the visibility of a timetable
 */
class UserTimetableViewModel(
    private val getUserTimetableUseCase: GetUserTimetableUseCase,
    private val changeEventVisibilityUseCase: ChangeEventVisibilityUseCase,
    private val changeEventNotificationUseCase: ChangeEventNotificationUseCase,
    private val deletePersonalEventUseCase: DeletePersonalEventUseCase,
    private val getCurrentWeekUseCase: GetCurrentWeekUseCase,
    private val analyticsLogger: AnalyticsLogger,
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
    private val _uiState = MutableStateFlow(TimetableUiState(isLoading = true))
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
            val errorStatus = resource.status.toErrorStatus()
            if (errorStatus != null && _uiState.value.errorStatus == null) {
                analyticsLogger.logEvent(
                    event = AnalyticsEvent.TIMETABLE_LOAD_ERROR,
                    params = mapOf(
                        AnalyticsParameter.TIMETABLE_TYPE to AnalyticsTimetableType.USER,
                        AnalyticsParameter.ERROR_TYPE to errorStatus.name,
                    ),
                )
            }

            _uiState.update {
                it.copy(
                    isLoading = resource.status.isLoading(),
                    isEmpty = resource.status.isEmpty(),
                    errorStatus = errorStatus,
                    events = resource.payload?.toImmutableList() ?: persistentListOf()
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

            _uiState.update { it.copy(selectedFrequency = frequency) }
        }
    }

    /**
     * Selects a frequency for the timetable.
     * This updates the UI state with the newly selected frequency.
     * @param frequency The frequency to select.
     */
    fun selectFrequency(frequency: Frequency) {
        logger.d(TAG, "selectFrequency: $frequency")
        analyticsLogger.logEvent(
            event = AnalyticsEvent.WEEK_FILTER_CHANGED,
            params = mapOf(
                AnalyticsParameter.TIMETABLE_TYPE to AnalyticsTimetableType.USER,
                AnalyticsParameter.FREQUENCY to frequency.name,
            ),
        )
        _uiState.update { it.copy(selectedFrequency = frequency) }
    }

    /**
     * Toggles the edit mode for the timetable.
     * This updates the UI state to reflect whether edit mode is currently on or off.
     */
    fun changeEditMode() {
        if (!_uiState.value.isEditModeOn) {
            analyticsLogger.logEvent(AnalyticsEvent.EDIT_MODE_OPENED)
        }

        _uiState.update {
            logger.d(TAG, "changeEditMode to: ${!it.isEditModeOn}")
            it.copy(isEditModeOn = !it.isEditModeOn)
        }
    }

    /**
     * Records that the timetable was opened from a notification for [eventId]. Each event id is
     * reported only once per ViewModel, so recompositions or configuration changes that replay
     * the same deep link argument aren't counted again.
     */
    fun logNotificationOpen(eventId: String) {
        logger.d(TAG, "Opened from notification for event $eventId")
        analyticsLogger.logEvent(AnalyticsEvent.NOTIFICATION_OPENED)
    }

    /**
     * Changes the visibility of a specific timetable class.
     * This updates the visibility status of the class in the UI state and calls the use case to persist the change.
     * @param event The timetable class whose visibility is to be changed.
     */
    fun changeTimetableClassVisibility(event: TimetableListItem.Event) {
        viewModelScope.launch {
            logger.d(TAG, "changeTimetableClassVisibility event: $event")
            changeEventVisibilityUseCase(event.id)
        }

        _uiState.update { state ->
            val newEvents = state.events.map {
                when {
                    it.id != event.id -> it
                    else -> it.copy(isVisible = !it.isVisible)
                }
            }.toImmutableList()

            state.copy(events = newEvents)
        }
    }

    fun changeTimetableClassNotification(event: TimetableListItem.Event) {
        viewModelScope.launch {
            logger.d(TAG, "changeTimetableClassNotification event: $event")
            changeEventNotificationUseCase(event.id)
        }

        _uiState.update { state ->
            val newEvents = state.events.map {
                when {
                    it.id != event.id -> it
                    else -> it.copy(isNotificationOn = !it.isNotificationOn)
                }
            }.toImmutableList()

            state.copy(events = newEvents)
        }
    }

    fun removeItem(event: TimetableListItem.Event) {
        viewModelScope.launch {
            logger.d(TAG, "deletePersonalEventUseCase event: $event")
            deletePersonalEventUseCase(event.id)
        }

        _uiState.update { state ->
            val newEvents = state.events.toMutableList().apply {
                removeAll { it.id == event.id }
            }.toImmutableList()

            state.copy(events = newEvents)
        }
    }

    /**
     * Retries loading the timetable.
     * This cancels the current job and starts a new one to load the timetable again.
     */
    fun retry() {
        logger.d(TAG, "retry")
        analyticsLogger.logEvent(
            event = AnalyticsEvent.TIMETABLE_RETRY,
            params = mapOf(AnalyticsParameter.TIMETABLE_TYPE to AnalyticsTimetableType.USER),
        )
        job.cancel()
        job = loadTimetable()
    }

    companion object {
        private const val TAG = "UserTimetableViewModel"
    }
}