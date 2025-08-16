package com.ubb.fmi.orar.feature.timetable.ui.model

import com.ubb.fmi.orar.data.core.model.ClassType

sealed interface TimetableItem {

    data class Divider(
        val day: String
    ) : TimetableItem

    data class Class(
        val startHour: String,
        val endHour: String,
        val subject: String,
        val classType: ClassType,
        val participant: String,
        val teacher: String,
        val room: String,
    ) : TimetableItem
}
