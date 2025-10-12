package com.ubb.fmi.orar.data.timetable.model

/**
 * Data class for a single entry in a [Timetable]
 * @property id: unique id for identification
 * @property configurationId: id for configuration affinity
 * @property ownerId: id of the owner that owns the [Timetable]
 * @property day: day in which the event takes place
 * @property frequency: frequency at which the event takes place
 * @property startHour: hour at which the event starts
 * @property endHour: hour at which the event ends
 * @property location: location at which the event takes place
 * @property activity: activity during event
 * @property type: type of event
 * @property participant: participant at the event
 * @property host: host at the event
 * @property isVisible: whether the event is visible on main page or not
 */
data class Event(
    val id: String,
    val configurationId: String,
    val ownerId: String,
    val day: Day,
    val frequency: Frequency,
    val startHour: Int,
    val endHour: Int,
    val location: Location?,
    val activity: Activity,
    val type: EventType,
    val participant: Participant?,
    val host: Host?,
    val isVisible: Boolean,
)
