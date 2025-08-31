package com.ubb.fmi.orar.domain.timetable.model

enum class TimetableOwnerType(val id: String) {
    ROOM("room"),
    STUDY_LINE("study_line"),
    SUBJECT("subject"),
    TEACHER("teacher");

    companion object {
        fun getById(id: String): TimetableOwnerType {
            return TimetableOwnerType.entries.firstOrNull { it.id == id } ?: ROOM
        }
    }
}
