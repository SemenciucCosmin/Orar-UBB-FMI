package com.ubb.fmi.orar.data.model

enum class ClassType(val id: String) {
    COURSE(id = "Curs"),
    SEMINARY(id = "Seminar"),
    LABORATORY(id = "Laborator");

    companion object {
        fun getById(id: String): ClassType {
            return entries.firstOrNull { it.id == id } ?: COURSE
        }
    }
}
