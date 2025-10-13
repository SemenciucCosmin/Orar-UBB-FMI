package com.ubb.fmi.orar.data.timetable.model

/**
 * Data class for a single entry in a [Timetable]
 * @property id: unique id for identification
 * @property configurationId: id for configuration affinity
 * @property day: day in which the event takes place
 * @property frequency: frequency at which the event takes place
 * @property startHour: hour at which the event starts
 * @property endHour: hour at which the event ends
 * @property location: location at which the event takes place
 * @property activity: activity during event
 * @property type: type of event
 * @property participant: participant at the event
 * @property caption: short description
 * @property details: additional information about the event
 * @property isVisible: whether the event is visible on main page or not
 */
data class Event(
    val id: String,
    val configurationId: String,
    val day: Day,
    val frequency: Frequency,
    val startHour: Int,
    val endHour: Int,
    val location: String,
    val activity: String,
    val type: EventType,
    val participant: String,
    val caption: String,
    val details: String,
    val isVisible: Boolean,
)
