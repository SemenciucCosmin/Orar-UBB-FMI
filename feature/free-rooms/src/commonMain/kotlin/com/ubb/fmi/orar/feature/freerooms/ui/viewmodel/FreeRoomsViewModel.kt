package com.ubb.fmi.orar.feature.freerooms.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.rooms.repository.RoomsRepository
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.domain.analytics.AnalyticsLogger
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent
import com.ubb.fmi.orar.feature.freerooms.ui.viewmodel.model.FreeRoomsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the state of the Free Rooms Search feature.
 */
class FreeRoomsViewModel(
    analyticsLogger: AnalyticsLogger,
    private val roomsRepository: RoomsRepository,
    private val logger: Logger,
) : ViewModel() {

    /**
     * Mutable state flow representing the UI state of the Free Rooms Search feature.
     * It holds the list of rooms, loading state, and error state.
     */
    private val _uiState = MutableStateFlow(FreeRoomsUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    /**
     * Initializes the ViewModel and starts fetching rooms.
     * The initial state is set to loading.
     */
    init {
        analyticsLogger.logEvent(AnalyticsEvent.FREE_ROOM_SEARCH)
        getItems()
    }

    /**
     * Fetches the list of rooms and events from the data source and updates the UI state.
     */
    private fun getItems() {
        viewModelScope.launch {
            logger.d(TAG, "getItems")
            roomsRepository.getAllRoomsAndEvents().collectLatest { (rooms, events) ->
                logger.d(TAG, "getItems rooms: ${rooms.size}")
                logger.d(TAG, "getItems events: ${events.size}")

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        rooms = rooms,
                        events = events
                    )
                }
            }
        }
    }

    fun changeStartHour(startHour: Int) {
        _uiState.update { it.copy(startHour = startHour) }
    }

    fun changeStartMinute(startMinute: Int) {
        _uiState.update { it.copy(startMinute = startMinute) }
    }

    fun changeEndHour(endHour: Int) {
        _uiState.update { it.copy(endHour = endHour) }
    }

    fun changeEndMinute(endMinute: Int) {
        _uiState.update { it.copy(endMinute = endMinute) }
    }

    fun selectDays(days: List<Day>) {
        _uiState.update { it.copy(selectedDays = days) }
    }

    companion object {
        private const val TAG = "FreeRoomsViewModel"
    }
}
