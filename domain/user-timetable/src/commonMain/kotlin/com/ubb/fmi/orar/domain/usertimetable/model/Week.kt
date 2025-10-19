package com.ubb.fmi.orar.domain.usertimetable.model

/**
 * Represents the teaching week
 */
enum class Week(val id: Int) {
    EVEN(id = 0),
    ODD(id = 1);

    companion object {
        fun getById(id: Int): Week {
            return entries.firstOrNull { it.id == id } ?: EVEN
        }
    }
}