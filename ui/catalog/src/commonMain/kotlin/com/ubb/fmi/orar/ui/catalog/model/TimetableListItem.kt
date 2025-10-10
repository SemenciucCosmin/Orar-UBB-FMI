package com.ubb.fmi.orar.ui.catalog.model

import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Location
import com.ubb.fmi.orar.data.timetable.model.Day

/**
 * Represents an item in the timetable list, which can either be a class or a divider for a specific day.
 * This interface is used to define the structure of items displayed in the timetable.
 */
sealed interface TimetableListItem {

    /**
     * Represents a divider in the timetable list, indicating a new day.
     *
     * @property day The day for which the divider is created.
     */
    data class Divider(
        val day: Day
    ) : TimetableListItem

    /**
     * Represents a class in the timetable list.
     *
     * @property id The unique identifier for the class.
     * @property startHour The starting hour of the class.
     * @property endHour The ending hour of the class.
     * @property title The subject of the class.
     * @property classType The type of the class (e.g., lecture, lab).
     * @property participant The participant in the class (e.g., student name).
     * @property teacher The teacher conducting the class.
     * @property room The room where the class is held.
     * @property isVisible Indicates whether the class is visible in the timetable.
     */
    data class Event(
        val id: String,
        val startHour: Int,
        val endHour: Int,
        val location: Location?,
        val title: String,
        val type: EventType,
        val participantName: String,
        val hostName: String,
        val isVisible: Boolean
    ) : TimetableListItem
}