package com.ubb.fmi.orar.data.timetable.model

/**
 * Generic data class for a timetable model
 * @param [owner]: owner of the timetable (Room, StudyLine, Subject, Teacher)
 * @param [events]: list of [Event] that belong to this timetable
 */
data class Timetable<Owner : com.ubb.fmi.orar.data.timetable.model.Owner>(
    val owner: Owner,
    val events: List<Event>
)
