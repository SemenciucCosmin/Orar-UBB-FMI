package com.ubb.fmi.orar.feature.roomtimetable.ui.viewmodel

import Logger
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.network.model.isEmpty
import com.ubb.fmi.orar.data.network.model.isLoading
import com.ubb.fmi.orar.data.rooms.repository.RoomsRepository
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.usertimetable.model.Week
import com.ubb.fmi.orar.domain.usertimetable.usecase.AdoptEventUseCase
import com.ubb.fmi.orar.domain.usertimetable.usecase.GetCurrentWeekUseCase
import com.ubb.fmi.orar.ui.catalog.extensions.toErrorStatus
import com.ubb.fmi.orar.ui.catalog.model.TimetableUiEvent
import com.ubb.fmi.orar.ui.catalog.viewmodel.EventViewModel
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.TimetableUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * ViewModel for the Room Timetable screen.
 * It fetches the timetable data for a specific room and manages the UI state.
 */
class RoomTimetableViewModel(
    private val roomId: String,
    private val roomsRepository: RoomsRepository,
    private val getCurrentWeekUseCase: GetCurrentWeekUseCase,
    private val adoptEventUseCase: AdoptEventUseCase,
    private val logger: Logger,
) : EventViewModel<TimetableUiEvent>() {

    /**
     * Mutable state flow that holds the UI state for the timetable.
     * It is updated with loading status, error status, and fetched classes.
     */
    private val _uiState = MutableStateFlow(TimetableUiState(isLoading = true))
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
     * Initializes the ViewModel and starts loading the timetable data.
     * The timetable is loaded when the ViewModel is created.
     */
    private fun loadTimetable() {
        viewModelScope.launch {
            logger.d(TAG, "loadTimetable room: $roomId")
            _uiState.update { it.copy(isLoading = true, errorStatus = null) }

            roomsRepository.getTimetable(roomId).collectLatest { resource ->
                val events = resource.payload?.events?.map {
                    it.copy(isVisible = true)
                }?.toImmutableList() ?: persistentListOf()

                _uiState.update {
                    it.copy(
                        isLoading = resource.status.isLoading(),
                        isEmpty = resource.status.isEmpty(),
                        errorStatus = resource.status.toErrorStatus(),
                        events = events,
                        title = resource.payload?.owner?.name ?: String.BLANK
                    )
                }
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
     * This updates the UI state with the selected frequency.
     *
     * @param frequency The frequency to be selected.
     */
    fun selectFrequency(frequency: Frequency) {
        logger.d(TAG, "selectFrequency: $frequency")
        _uiState.update { it.copy(selectedFrequency = frequency) }
    }

    /**
     * Retries loading the timetable data.
     * This is typically called when the user wants to refresh the timetable after an error.
     */
    fun retry() {
        logger.d(TAG, "retry")
        loadTimetable()
    }

    /**
     * Add new event to user timetable
     */
    fun addEvent(eventId: String) {
        viewModelScope.launch {
            adoptEventUseCase(eventId)
            registerEvent(TimetableUiEvent.EVENT_ADOPTION_SUCCESSFUL)
        }
    }

    companion object {
        private const val TAG = "RoomTimetableViewModel"
    }
}
