package com.ubb.fmi.orar.ui.catalog.model

import com.ubb.fmi.orar.domain.timetable.model.TimetableOwnerType
import com.ubb.fmi.orar.domain.timetable.model.ClassType

sealed interface TimetableListItem {

    data class Divider(
        val day: String
    ) : TimetableListItem

    data class Class(
        val id: String,
        val startHour: String,
        val endHour: String,
        val subject: String,
        val classType: ClassType,
        val timetableOwnerType: TimetableOwnerType,
        val participant: String,
        val teacher: String,
        val room: String,
        val isVisible: Boolean
    ) : TimetableListItem
}