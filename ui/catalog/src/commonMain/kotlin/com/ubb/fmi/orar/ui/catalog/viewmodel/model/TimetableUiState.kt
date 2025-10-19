package com.ubb.fmi.orar.ui.catalog.viewmodel.model

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
import kotlin.String
import kotlin.comparisons.compareBy
import kotlin.time.ExperimentalTime

/**
 * Represents the UI state of the timetable, including the list of classes, title, study level,
 * group, selected frequency, edit mode status, loading status, and error status.
 * This state is used to manage the display and interaction of the timetable in the UI.
 * @property events The list of events in the timetable.
 * @property title The title of the timetable, typically representing the academic program or semester.
 * @property studyLevel The study level associated with the timetable, such as first year, second year, etc.
 * @property group The group identifier for the classes in the timetable.
 * @property selectedFrequency The frequency of classes to be displayed, such as weekly or bi-weekly.
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
) {
    companion object {
        /**
         * Creates an initial state for the timetable UI.
         * This state is used when the timetable is first loaded or reset.
         */
        @OptIn(ExperimentalTime::class)
        val TimetableUiState.timetableListItems: ImmutableList<TimetableListItem>
            get() {
                val filteredEvents = events.filter { event ->
                    event.frequency.id in listOf(Frequency.BOTH.id, selectedFrequency.id)
                }.sortedWith(
                    compareBy<Event> { it.day.orderIndex }
                        .thenBy { it.startHour }
                        .thenBy { it.endHour }
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
                                    isPersonal = event.ownerId == Owner.User.id
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
    }
}