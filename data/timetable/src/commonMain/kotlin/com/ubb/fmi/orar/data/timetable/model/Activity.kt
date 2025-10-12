package com.ubb.fmi.orar.data.timetable.model

/**
 * Data class for activity model that takes place during an [Event]
 * @property id: unique id for identification
 * @property name: name of activity
 */
data class Activity(
    val id: String,
    val name: String,
)
