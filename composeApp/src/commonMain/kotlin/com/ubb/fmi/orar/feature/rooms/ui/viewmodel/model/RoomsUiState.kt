package com.ubb.fmi.orar.feature.rooms.ui.viewmodel.model

import com.ubb.fmi.orar.data.rooms.model.Room

data class RoomsUiState(
    val rooms: List<Room> = emptyList(),
    val isLoading: Boolean = true,
    val isError: Boolean = true
)
