package com.ubb.fmi.orar.ui.catalog.model

import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.EventType

/**
 * Represents an item in the timetable list, which can either be a class or a divider for a specific day.
 * This interface is used to define the structure of items displayed in the timetable.
 */
sealed interface TimetableListItem {

    /**
     * Represents a divider in the timetable list, indicating a new day.
     */
    data class Divider(
        val day: Day
    ) : TimetableListItem

    /**
     * Represents an event in the timetable list.
     */
    data class Event(
        val id: String,
        val startHour: Int,
        val endHour: Int,
        val location: String,
        val title: String,
        val type: EventType,
        val participant: String,
        val caption: String,
        val details: String,
        val isVisible: Boolean
    ) : TimetableListItem
}