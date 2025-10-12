package com.ubb.fmi.orar.feature.rooms.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Represents the UI state for the Rooms feature.
 * Contains a list of rooms, loading state, and error state.
 *
 * @property rooms The list of rooms to be displayed.
 * @property isLoading Indicates whether the data is currently being loaded.
 * @property errorStatus Indicates whether there was an error loading the data.
 */
data class RoomsUiState(
    val rooms: ImmutableList<Owner.Room> = persistentListOf(),
    val isLoading: Boolean = true,
    val errorStatus: ErrorStatus? = null,
)
