package com.ubb.fmi.orar.data.timetable.model

/**
 * Data class for participant model that participates the [Event]
 * @property id: unique id for identification
 * @property name: name of participant
 */
data class Participant(
    val id: String,
    val name: String,
)
