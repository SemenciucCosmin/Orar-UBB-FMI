package com.ubb.fmi.orar.ui.catalog.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.StudyLevel
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.COMMA
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus
import com.ubb.fmi.orar.ui.catalog.model.TimetableListItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.String
import kotlin.comparisons.compareBy
import kotlin.time.Clock

/**
 * Represents the UI state of the timetable, including the list of classes, title, study level,
 * group, selected frequency, edit mode status, loading status, and error status.
 * This state is used to manage the display and interaction of the timetable in the UI.
 * @property events The list of events in the timetable.
 * @property title The title of the timetable, typically representing the academic program or semester.
 * @property studyLevel The study level associated with the timetable, such as first year, second year, etc.
 * @property group The group identifier for the classes in the timetable.
 * @property selectedFrequency The frequency of classes to be displayed, such as weekly or bi-weekly.
 * @property currentFrequency The week type of the current calendar week, if known.
 * @property isEditModeOn Indicates whether the timetable is in edit mode, allowing modifications
 * @property isLoading Indicates whether the timetable data is currently being loaded.
 * @property errorStatus Indicates whether there was an error loading the timetable data.
 */
data class TimetableUiState(
    val events: ImmutableList<Event> = persistentListOf(),
    val title: String = String.BLANK,
    val studyLevel: StudyLevel? = null,
    val group: String? = null,
    val selectedFrequency: Frequency = Frequency.WEEK_1,
    val isEditModeOn: Boolean = false,
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val errorStatus: ErrorStatus? = null,
    private val currentFrequency: Frequency? = null,
) {
    /**
     * Creates an initial state for the timetable UI.
     * This state is used when the timetable is first loaded or reset.
     */
    val timetableListItems: ImmutableList<TimetableListItem>
        get() {
            val filteredEvents = events.filter { event ->
                event.frequency.id in listOf(Frequency.BOTH.id, selectedFrequency.id)
            }.sortedWith(
                compareBy<Event> { it.day.orderIndex }
                    .thenBy { it.startHour }
                    .thenBy { it.endHour }
                    .thenBy { it.activity }
            )

            val groupedEvents = filteredEvents.groupBy { it.day }.mapKeys { (day, _) ->
                TimetableListItem.Divider(day)
            }

            val timetableItems = groupedEvents.mapValues { (_, events) ->
                when {
                    isEditModeOn -> {
                        events.map { event ->
                            TimetableListItem.Event(
                                id = event.id,
                                day = event.day,
                                startHour = event.startHour,
                                startMinute = event.startMinute,
                                endHour = event.endHour,
                                endMinute = event.endMinute,
                                location = event.location,
                                title = event.activity,
                                type = event.type,
                                participant = event.participant,
                                caption = event.caption,
                                details = event.details,
                                isVisible = event.isVisible,
                                isNotificationOn = event.isNotificationOn,
                                isPersonal = event.ownerId == Owner.User.id,
                            )
                        }
                    }

                    else -> {
                        val visibleEvents = events.filter { it.isVisible }
                        val groupedEvents = visibleEvents.groupBy { event ->
                            listOf(
                                event.day,
                                event.startHour,
                                event.endHour,
                                event.location,
                                event.activity,
                                event.type,
                                event.caption,
                            )
                        }

                        groupedEvents.values.mapNotNull { events ->
                            val joinedParticipantName = events.joinToString(
                                String.COMMA + String.SPACE
                            ) { it.participant }

                            val event = events.firstOrNull() ?: return@mapNotNull null

                            TimetableListItem.Event(
                                id = event.id,
                                day = event.day,
                                startHour = event.startHour,
                                startMinute = event.startMinute,
                                endHour = event.endHour,
                                endMinute = event.endMinute,
                                location = event.location,
                                title = event.activity,
                                type = event.type,
                                participant = joinedParticipantName,
                                caption = event.caption,
                                details = event.details,
                                isVisible = event.isVisible,
                                isNotificationOn = event.isNotificationOn,
                                isPersonal = event.ownerId == Owner.User.id
                            )
                        }
                    }
                }
            }

            return timetableItems.filter { (_, events) ->
                events.isNotEmpty()
            }.map { (day, events) ->
                listOf(day) + events
            }.flatten().toImmutableList()
        }

    /**
     * Ids of the displayed events taking place right now.
     */
    val ongoingEventIds: ImmutableList<String>
        get() {
            val currentMinutes = currentMinutes
            return todayEvents.filter { event ->
                currentMinutes in event.startMinutes until event.endMinutes
            }.map { it.id }.toImmutableList()
        }

    /**
     * Ids of the displayed events starting later today.
     */
    val upcomingEventIds: ImmutableList<String>
        get() {
            val currentMinutes = currentMinutes
            return todayEvents.filter { event ->
                event.startMinutes > currentMinutes
            }.map { it.id }.toImmutableList()
        }

    val currentDayIndex: Int
        get() {
            val currentDayIndex = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .dayOfWeek
                .ordinal

            return timetableListItems.indexOfFirst {
                it is TimetableListItem.Divider && it.day.orderIndex >= currentDayIndex
            }.takeIf { it >= FIRST_INDEX } ?: FIRST_INDEX
        }

    /**
     * Today, if the current week is displayed; null otherwise.
     */
    val currentDay: Day?
        get() {
            if (selectedFrequency != currentFrequency) return null

            val currentDayIndex = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .dayOfWeek
                .ordinal

            return Day.entries.firstOrNull { it.orderIndex == currentDayIndex }
        }

    /**
     * Today's displayed events. Empty unless the current week is displayed.
     */
    private val todayEvents: List<TimetableListItem.Event>
        get() {
            val currentDay = currentDay ?: return emptyList()

            return timetableListItems
                .filterIsInstance<TimetableListItem.Event>()
                .filter { it.day == currentDay }
        }

    /**
     * Current time, in minutes since midnight.
     */
    private val currentMinutes: Int
        get() {
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            return now.hour * MINUTES_PER_HOUR + now.minute
        }

    private val TimetableListItem.Event.startMinutes: Int
        get() = startHour * MINUTES_PER_HOUR + startMinute

    private val TimetableListItem.Event.endMinutes: Int
        get() = endHour * MINUTES_PER_HOUR + endMinute

    companion object {
        private const val FIRST_INDEX = 0
        private const val MINUTES_PER_HOUR = 60
    }
}