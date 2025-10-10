package com.ubb.fmi.orar.data.timetable.model

enum class EventType(val id: String) {
    LECTURE(id = "Curs"),
    SEMINARY(id = "Seminar"),
    LABORATORY(id = "Laborator"),
    STAFF(id = "Colectiv");

    companion object {
        fun getById(id: String): EventType {
            return EventType.entries.firstOrNull { it.id == id } ?: LECTURE
        }
    }
}
