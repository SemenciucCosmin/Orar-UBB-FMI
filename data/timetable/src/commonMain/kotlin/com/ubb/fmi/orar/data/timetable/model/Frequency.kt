package com.ubb.fmi.orar.data.timetable.model

/**
 * Represents the frequency of events in the timetable.
 */
enum class Frequency(val id: String) {
    WEEK_1(id = "sapt. 1"),
    WEEK_2(id = "sapt. 2"),
    BOTH(id = "both");

    companion object {
        fun getById(id: String): Frequency {
            return entries.firstOrNull { it.id == id } ?: BOTH
        }
    }
}
