package com.ubb.fmi.orar.data.notifications.model

import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency

/**
 * Platform-agnostic representation of a single timetable [com.ubb.fmi.orar.data.timetable.model.Event]
 * to schedule/cancel a local notification for. [id] must match the originating event's id, since
 * it's reused as the notification/alarm identifier so it can be individually cancelled later.
 * [advanceMinutes] is the user-configured lead time; the notification fires that many minutes
 * before [startHour]:[startMinute], not at the event's start itself.
 */
data class EventNotification(
    val id: String,
    val activity: String,
    val type: EventType,
    val location: String,
    val participant: String,
    val frequency: Frequency,
    val day: Day,
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
    val advanceMinutes: Int,
)
