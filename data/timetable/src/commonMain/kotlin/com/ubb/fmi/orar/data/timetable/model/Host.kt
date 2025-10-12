package com.ubb.fmi.orar.data.timetable.model

/**
 * Data class for host model that hosts the [Event]
 * @property id: unique id for identification
 * @property name: name of host
 */
data class Host(
    val id: String,
    val name: String,
)
