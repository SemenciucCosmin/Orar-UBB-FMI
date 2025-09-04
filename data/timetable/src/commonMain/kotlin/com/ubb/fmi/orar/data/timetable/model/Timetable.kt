package com.ubb.fmi.orar.data.timetable.model

/**
 * Generic data class for a timetable model
 * @param [owner]: owner of the timetable (Room, StudyLine, Subject, Teacher)
 * @param [classes]: list of [TimetableClass] that belong to this timetable
 */
data class Timetable<Owner : TimetableOwner>(
    val owner: Owner,
    val classes: List<TimetableClass>
)
