package com.ubb.fmi.orar.feature.freerooms.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.Owner
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalTime

/**
 * Represents the UI state for the Free Rooms Search feature.
 */
data class FreeRoomsUiState(
    private val rooms: List<Owner.Room> = emptyList(),
    private val events: List<Event> = emptyList(),
    val startHour: Int = DEFAULT_START_HOUR,
    val startMinute: Int = DEFAULT_MINUTE,
    val endHour: Int = DEFAULT_END_HOUR,
    val endMinute: Int = DEFAULT_MINUTE,
    val selectedDays: List<Day> = emptyList(),
    val isLoading: Boolean = false,
) {
    companion object {
        private val FreeRoomsUiState.overlappingEvents: List<Event>
            get() = events.filter { event ->
                val isDayOverlap = selectedDays.isNotEmpty() && event.day in selectedDays

                val selectedStartMillis = LocalTime(startHour, startMinute).toMillisecondOfDay()
                val selectedEndMillis = LocalTime(endHour, endMinute).toMillisecondOfDay()

                val eventStartMillis = LocalTime(
                    hour = event.startHour,
                    minute = event.startMinute
                ).toMillisecondOfDay()

                val eventEndMillis = LocalTime(
                    hour = event.endHour,
                    minute = event.endMinute
                ).toMillisecondOfDay()

                val isEventOver = eventStartMillis in selectedStartMillis..<selectedEndMillis
                val isSelectionOver = selectedStartMillis in eventStartMillis..<eventEndMillis
                val isTimeOverlap = isEventOver || isSelectionOver

                isDayOverlap && isTimeOverlap
            }

        val FreeRoomsUiState.filteredRooms: ImmutableList<Owner.Room>
            get() {
                val overlappingRoomIds = overlappingEvents.map { it.ownerId }
                return rooms.filter { room ->
                    val roomHasEvents = events.any { it.ownerId == room.id }
                    val roomHasOverlap = room.id in overlappingRoomIds
                    roomHasEvents && !roomHasOverlap
                }.sortedBy { it.name }.toImmutableList()
            }

        private const val DEFAULT_START_HOUR = 12
        private const val DEFAULT_END_HOUR = 14
        private const val DEFAULT_MINUTE = 0
    }
}
