package com.ubb.fmi.orar.feature.timetable.ui.model

enum class GroupType (
    val id: String,
    val label: String
) {
    SEMIGROUP_1(
        id = "/1",
        label = "Semigrupa 1"
    ),
    SEMIGROUP_2(
        id = "/2",
        label = "Semigrupa 2"
    );

    companion object {
        fun getById(id: String): GroupType {
            return entries.firstOrNull { it.id == id } ?: SEMIGROUP_1
        }
    }
}