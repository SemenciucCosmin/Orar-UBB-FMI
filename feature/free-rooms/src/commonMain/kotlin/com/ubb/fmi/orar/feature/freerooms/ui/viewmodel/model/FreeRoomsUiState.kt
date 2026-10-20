package com.ubb.fmi.orar.feature.freerooms.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.Owner
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
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
    val selectedDays: ImmutableList<Day> = persistentListOf(),
    val selectedFrequency: Frequency = Frequency.BOTH,
    val isLoading: Boolean = false,
) {
    companion object {
        private val FreeRoomsUiState.startMillis: Int
            get() = LocalTime(startHour, startMinute).toMillisecondOfDay()

        private val FreeRoomsUiState.endMillis: Int
            get() = LocalTime(endHour, endMinute).toMillisecondOfDay()

        val FreeRoomsUiState.isTimeIntervalValid: Boolean
            get() = endMillis > startMillis

        /**
         * Events happening in one of the selected days, in the selected weeks, during the selected
         * interval. An event of a single week doesn't overlap a selection of the other week.
         */
        private val FreeRoomsUiState.overlappingEvents: List<Event>
            get() = events.filter { event ->
                val isDayOverlap = event.day in selectedDays

                val isFrequencyOverlap = selectedFrequency == Frequency.BOTH ||
                    event.frequency == Frequency.BOTH ||
                    event.frequency == selectedFrequency

                val eventStartMillis = LocalTime(
                    hour = event.startHour,
                    minute = event.startMinute
                ).toMillisecondOfDay()

                val eventEndMillis = LocalTime(
                    hour = event.endHour,
                    minute = event.endMinute
                ).toMillisecondOfDay()

                val isTimeOverlap = eventStartMillis < endMillis && startMillis < eventEndMillis

                isDayOverlap && isFrequencyOverlap && isTimeOverlap
            }

        /**
         * Rooms with a timetable that are free during the whole selection. Empty when there is no
         * valid selection.
         */
        val FreeRoomsUiState.filteredRooms: ImmutableList<Owner.Room>
            get() {
                if (selectedDays.isEmpty() || !isTimeIntervalValid) return persistentListOf()

                val roomIdsWithEvents = events.mapTo(mutableSetOf()) { it.ownerId }
                val overlappingRoomIds = overlappingEvents.mapTo(mutableSetOf()) { it.ownerId }

                return rooms.filter { room ->
                    room.id in roomIdsWithEvents && room.id !in overlappingRoomIds
                }.sortedBy { it.name }.toImmutableList()
            }

        private const val DEFAULT_START_HOUR = 12
        private const val DEFAULT_END_HOUR = 14
        private const val DEFAULT_MINUTE = 0
    }
}
