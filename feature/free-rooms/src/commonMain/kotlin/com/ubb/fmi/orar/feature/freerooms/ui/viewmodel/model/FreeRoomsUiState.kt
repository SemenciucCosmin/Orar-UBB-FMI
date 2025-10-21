package com.ubb.fmi.orar.feature.freerooms.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Represents the UI state for the Free Rooms Search feature.
 */
data class FreeRoomsUiState(
    val rooms: ImmutableList<Owner.Room> = persistentListOf(),
    val startHour: Int = DEFAULT_HOUR,
    val startMinute: Int = DEFAULT_MINUTE,
    val endHour: Int = DEFAULT_HOUR,
    val endMinute: Int = DEFAULT_MINUTE,
    val selectedFrequency: Frequency = Frequency.BOTH,
    val selectedDays: List<Day> = emptyList(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val errorStatus: ErrorStatus? = null,
) {
    companion object {
        private const val DEFAULT_HOUR = 12
        private const val DEFAULT_MINUTE = 0
    }
}
