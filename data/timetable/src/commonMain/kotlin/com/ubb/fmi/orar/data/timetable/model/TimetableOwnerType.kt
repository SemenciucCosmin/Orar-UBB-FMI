package com.ubb.fmi.orar.data.timetable.model

/**
 * Enum class for any type of timetable owner
 * @param [id]: unique identifier
 */
enum class TimetableOwnerType(val id: String) {
    ROOM("room"),
    STUDY_LINE("study_line"),
    SUBJECT("subject"),
    TEACHER("teacher");

    companion object {
        fun getById(id: String): TimetableOwnerType {
            return entries.firstOrNull { it.id == id } ?: ROOM
        }
    }
}