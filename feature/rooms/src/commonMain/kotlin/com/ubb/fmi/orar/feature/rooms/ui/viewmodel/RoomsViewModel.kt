package com.ubb.fmi.orar.feature.rooms.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.network.model.isError
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.feature.rooms.ui.viewmodel.model.RoomsUiState
import com.ubb.fmi.orar.ui.catalog.extensions.toErrorStatus
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus
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
 *
 * @property roomsDataSource The data source for fetching room information.
 * @property timetablePreferences Preferences related to the timetable configuration.
 */
class RoomsViewModel(
    private val roomsDataSource: RoomsDataSource,
    private val timetablePreferences: TimetablePreferences,
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
    private val _uiState = MutableStateFlow(RoomsUiState())
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
     * It collects the timetable configuration and uses it to fetch the rooms.
     * The UI state is updated with loading, error, and room data accordingly.
     */
    private fun getRooms() = viewModelScope.launch {
        logger.d(TAG, "getRooms")
        _uiState.update { it.copy(isLoading = true, errorStatus = null) }

        timetablePreferences.getConfiguration().collectLatest { configuration ->
            logger.d(TAG, "getRooms configuration: $configuration")
            if (configuration == null) {
                _uiState.update { it.copy(isLoading = false, errorStatus = ErrorStatus.NOT_FOUND) }
                return@collectLatest
            }

            val resource = roomsDataSource.getOwners(
                year = configuration.year,
                semesterId = configuration.semesterId
            )

            logger.d(TAG, "getRooms resource: $resource")
            _uiState.update {
                it.copy(
                    isLoading = false,
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
