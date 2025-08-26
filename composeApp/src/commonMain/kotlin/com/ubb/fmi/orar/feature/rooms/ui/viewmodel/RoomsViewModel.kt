package com.ubb.fmi.orar.feature.rooms.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.feature.rooms.ui.viewmodel.model.RoomsUiState
import com.ubb.fmi.orar.network.model.isError
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class RoomsViewModel(
    private val roomsDataSource: RoomsDataSource,
    private val timetablePreferences: TimetablePreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoomsUiState())
    val uiState = _uiState.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )


    init {
        getRooms()
    }

    private fun getRooms() = viewModelScope.launch {
        _uiState.update {
            it.copy(
                isLoading = true,
                isError = false
            )
        }

        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        val roomResource = roomsDataSource.getRooms(
            year = configuration?.year ?: return@launch,
            semesterId = configuration.semesterId
        )

        _uiState.update {
            it.copy(
                isLoading = false,
                isError = roomResource.status.isError(),
                rooms = roomResource.payload?.toImmutableList() ?: persistentListOf()
            )
        }
    }

    fun retry() {
        getRooms()
    }
}
