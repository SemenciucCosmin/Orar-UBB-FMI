package com.ubb.fmi.orar.feature.freerooms.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.network.model.isEmpty
import com.ubb.fmi.orar.data.network.model.isLoading
import com.ubb.fmi.orar.data.rooms.repository.RoomsRepository
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.feature.freerooms.ui.viewmodel.model.FreeRoomsUiState
import com.ubb.fmi.orar.ui.catalog.extensions.toErrorStatus
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the state of the Free Rooms Search feature.
 */
class FreeRoomsViewModel(
    private val roomsRepository: RoomsRepository,
    private val logger: Logger,
) : ViewModel() {

    /**
     * Job to manage the coroutine for fetching rooms.
     * This allows for cancellation and restarting of the job when needed.
     */
    private var job: Job

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
        job = getRooms()
    }

    /**
     * Fetches the list of rooms from the data source and updates the UI state.
     */
    private fun getRooms() = viewModelScope.launch {
        logger.d(TAG, "getRooms")
        _uiState.update { it.copy(isLoading = true, errorStatus = null) }

        roomsRepository.getRooms().collectLatest { resource ->
            logger.d(TAG, "getRooms resource: $resource")

            _uiState.update {
                it.copy(
                    isLoading = resource.status.isLoading(),
                    isEmpty = resource.status.isEmpty(),
                    errorStatus = resource.status.toErrorStatus(),
                    rooms = resource.payload?.toImmutableList() ?: persistentListOf()
                )
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

    fun selectFrequency(frequency: Frequency) {
        _uiState.update { it.copy(selectedFrequency = frequency) }
    }

    fun selectDays(days: List<Day>) {
        _uiState.update { it.copy(selectedDays = days) }
    }

    /**
     * Cancels the current job and restarts the fetching of rooms.
     * This is useful for retrying the fetch operation in case of an error.
     */
    fun retry() {
        logger.d(TAG, "retry")
        job.cancel()
        job = getRooms()
    }

    companion object {
        private const val TAG = "FreeRoomsViewModel"
    }
}
