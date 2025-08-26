package com.ubb.fmi.orar.domain.timetable.model

enum class Degree(
    val id: String,
    val label: String,
) {
    LICENCE(
        id = "licenta",
        label = "Licenta"
    ),
    MASTER(
        id = "master",
        label = "Master"
    )
}