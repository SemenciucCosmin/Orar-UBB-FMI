package com.ubb.fmi.orar.feature.rooms.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.TimetableOwner
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class RoomsUiState(
    val rooms: ImmutableList<TimetableOwner.Room> = persistentListOf(),
    val isLoading: Boolean = true,
    val isError: Boolean = true
)
