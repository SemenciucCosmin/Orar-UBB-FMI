package com.ubb.fmi.orar.data.timetable.model

/**
 * Data class for location model at which an [Event] takes place
 * @property id: unique id for identification
 * @property name: name of location
 * @property address: address of location
 */
data class Location(
    val id: String,
    val name: String,
    val address: String,
)
