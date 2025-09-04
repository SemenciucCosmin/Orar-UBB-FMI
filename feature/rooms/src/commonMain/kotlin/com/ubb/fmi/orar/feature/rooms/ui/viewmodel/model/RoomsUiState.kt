package com.ubb.fmi.orar.feature.rooms.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.TimetableOwner
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Represents the UI state for the Rooms feature.
 * Contains a list of rooms, loading state, and error state.
 *
 * @property rooms The list of rooms to be displayed.
 * @property isLoading Indicates whether the data is currently being loaded.
 * @property isError Indicates whether there was an error loading the data.
 */
data class RoomsUiState(
    val rooms: ImmutableList<TimetableOwner.Room> = persistentListOf(),
    val isLoading: Boolean = true,
    val isError: Boolean = true
)
