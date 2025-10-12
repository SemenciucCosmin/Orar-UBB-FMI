package com.ubb.fmi.orar.domain.timetable.model

/**
 * Represents the two semesters in the academic year.
 * Each semester is identified by a unique ID.
 */
enum class Semester(val id: String) {
    FIRST(id = "1"),
    SECOND(id = "2");

    companion object {
        fun getById(id: String): Semester {
            return entries.firstOrNull { it.id == id } ?: FIRST
        }
    }
}
