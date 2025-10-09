package com.ubb.fmi.orar.data.timetable.model

/**
 * Data class for any timetable class model
 * @param [id]: unique id
 * @param [day]: day on which the class takes place
 * @param [startHour]: hour at which the class starts
 * @param [endHour]: hour at which the class ends
 * @param [frequencyId]: frequency at which the class takes place (Week1, Week2 or both)
 * @param [studyLine]: study line to which the class belongs
 * @param [room]: room in which the class takes place
 * @param [participant]: participant to the class (a group, whole year ...)
 * @param [classType]: type of the class (Lecture, Seminary, Laboratory, Staff)
 * @param [ownerId]: owner of the class (a room, a teacher, a study line ...)
 * @param [subject]: subject which takes place during the class
 * @param [teacher]: teacher that hosts the class
 * @param [isVisible]: determines the visibility of this class on the users timetable
 * @param [configurationId]: configuration id of which this class belongs to
 */
data class TimetableClass(
    val id: String,
    val day: String,
    val startHour: String,
    val endHour: String,
    val frequencyId: String,
    val room: String,
    val field: String,
    val participant: String,
    val classType: String,
    val ownerId: String,
    val subject: String,
    val teacher: String,
    val isVisible: Boolean,
    val configurationId: String,
)
