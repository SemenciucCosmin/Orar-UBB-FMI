package com.ubb.fmi.orar.ui.catalog.model

enum class Frequency(
    val id: String,
    val label: String
) {
    WEEK_1(
        id = "sapt. 1",
        label = "sapt. 1",
    ),
    WEEK_2(
        id = "sapt. 2",
        label = "sapt. 2",
    ),
    BOTH(
        id = "null",
        label = "",
    );

    companion object {
        fun getById(id: String): Frequency {
            return entries.firstOrNull { it.id == id } ?: BOTH
        }
    }
}