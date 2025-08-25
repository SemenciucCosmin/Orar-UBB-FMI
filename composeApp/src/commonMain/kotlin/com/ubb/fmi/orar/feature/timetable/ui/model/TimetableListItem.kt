package com.ubb.fmi.orar.feature.timetable.ui.model

import com.ubb.fmi.orar.domain.timetable.model.ClassType

sealed interface TimetableListItem {

    data class Divider(
        val day: String
    ) : TimetableListItem

    data class Class(
        val startHour: String,
        val endHour: String,
        val subject: String,
        val classType: ClassType,
        val participant: String,
        val teacher: String,
        val room: String,
    ) : TimetableListItem
}