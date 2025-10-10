package com.ubb.fmi.orar.data.timetable.model


enum class Degree(val id: String) {
    LICENCE(id = "licenta"),
    MASTER(id = "master");

    companion object {
        fun getById(id: String): Degree {
            return Degree.entries.firstOrNull { it.id == id } ?: LICENCE
        }
    }
}