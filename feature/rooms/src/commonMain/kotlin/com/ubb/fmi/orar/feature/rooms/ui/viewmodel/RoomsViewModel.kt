package com.ubb.fmi.orar.feature.rooms.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.network.model.isEmpty
import com.ubb.fmi.orar.data.network.model.isLoading
import com.ubb.fmi.orar.data.rooms.repository.RoomsRepository
import com.ubb.fmi.orar.feature.rooms.ui.viewmodel.model.RoomsUiState
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
 * ViewModel for managing the state of the Rooms feature.
 * It fetches room data from the RoomsDataSource and updates the UI state accordingly.
 */
class RoomsViewModel(
    private val roomsRepository: RoomsRepository,
    private val logger: Logger,
) : ViewModel() {

    /**
     * Job to manage the coroutine for fetching rooms.
     * This allows for cancellation and restarting of the job when needed.
     */
    private var job: Job

    /**
     * Mutable state flow representing the UI state of the Rooms feature.
     * It holds the list of rooms, loading state, and error state.
     */
    private val _uiState = MutableStateFlow(RoomsUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    /**
     * Initializes the ViewModel and starts fetching rooms.
     * The initial state is set to loading.
     */
    init {
        job = getRooms()
    }

    /**
     * Sets the search query for filtering rooms.
     * This updates the UI state with the new search query, which will be used to
     * filter the rooms list.
     * @param searchQuery The new search query to set.
     */
    fun setSearchQuery(searchQuery: String) {
        logger.d(TAG, "setSearchQuery: $searchQuery")
        _uiState.update {
            it.copy(searchQuery = searchQuery)
        }
    }

    /**
     * Fetches the list of rooms from the data source and updates the UI state.
     * It collects the timetable configuration and uses it to fetch the rooms.
     * The UI state is updated with loading, error, and room data accordingly.
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
        private const val TAG = "RoomsViewModel"
    }
}
