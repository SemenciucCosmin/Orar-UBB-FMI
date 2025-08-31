package com.ubb.fmi.orar.feature.rooms.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.feature.rooms.ui.viewmodel.model.RoomsUiState
import com.ubb.fmi.orar.data.network.model.isError
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RoomsViewModel(
    private val roomsDataSource: RoomsDataSource,
    private val timetablePreferences: TimetablePreferences
) : ViewModel() {

    private var job: Job
    private val _uiState = MutableStateFlow(RoomsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        job = getRooms()
    }

    private fun getRooms() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, isError = false) }
        timetablePreferences.getConfiguration().collectLatest { configuration ->
            if (configuration == null) {
                _uiState.update { it.copy(isLoading = false, isError = true) }
                return@collectLatest
            }

            val resource = roomsDataSource.getOwners(
                year = configuration.year,
                semesterId = configuration.semesterId
            )

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = resource.status.isError(),
                    rooms = resource.payload?.toImmutableList() ?: persistentListOf()
                )
            }
        }
    }

    fun retry() {
        job.cancel()
        job = getRooms()
    }
}
