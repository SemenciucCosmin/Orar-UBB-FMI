package com.ubb.fmi.orar.data.model

enum class Frequency(val id: String) {
    WEEK_1(id = "sapt. 1"),
    WEEK_2(id = "sapt. 2"),
    BOTH(id = "");

    companion object {
        fun getById(id: String): Frequency {
            return entries.firstOrNull { it.id == id } ?: BOTH
        }
    }
}