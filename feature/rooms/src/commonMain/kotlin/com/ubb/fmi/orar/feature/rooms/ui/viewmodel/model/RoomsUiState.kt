package com.ubb.fmi.orar.feature.rooms.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * Represents the UI state for the Rooms feature.
 * Contains a list of rooms, loading state, and error state.
 *
 * @property rooms The list of rooms to be displayed.
 * @property searchQuery The current search query entered by the user.
 * @property isLoading Indicates whether the data is currently being loaded.
 * @property errorStatus Indicates whether there was an error loading the data.
 */
data class RoomsUiState(
    private val rooms: ImmutableList<Owner.Room> = persistentListOf(),
    val searchQuery: String = String.BLANK,
    val isLoading: Boolean = true,
    val errorStatus: ErrorStatus? = null,
) {
    companion object {
        /**
         * Filtered list of rooms by search query
         */
        val RoomsUiState.filteredRooms: ImmutableList<Owner.Room>
            get() {
                return rooms.filter { room ->
                    val isMatching = room.name.lowercase().contains(searchQuery.lowercase())
                    searchQuery.isBlank() || isMatching
                }.toImmutableList()
            }
    }
}
