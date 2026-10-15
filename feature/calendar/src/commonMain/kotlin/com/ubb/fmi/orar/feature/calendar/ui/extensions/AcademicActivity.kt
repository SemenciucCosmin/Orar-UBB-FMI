package com.ubb.fmi.orar.feature.calendar.ui.extensions

import com.ubb.fmi.orar.data.calendar.model.AcademicActivity
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem

val AcademicActivity.colorItem: ColorItem
    get() = when (this) {
        AcademicActivity.ACADEMIC_PREPARATION -> ColorItem.CALENDAR_PREPARATION
        AcademicActivity.TEACHING_ACTIVITY -> ColorItem.CALENDAR_TEACHING
        AcademicActivity.CHRISTMAS_HOLIDAY -> ColorItem.CALENDAR_HOLIDAY
        AcademicActivity.EASTER_HOLIDAY -> ColorItem.CALENDAR_HOLIDAY
        AcademicActivity.HOLIDAY -> ColorItem.CALENDAR_HOLIDAY
        AcademicActivity.EXAM_SESSION -> ColorItem.CALENDAR_SESSION
        AcademicActivity.RETAKE_SESSION -> ColorItem.CALENDAR_SESSION
        AcademicActivity.INTERNSHIP_PERIOD -> ColorItem.CALENDAR_INTERNSHIP
        AcademicActivity.THESIS_PREPARATION -> ColorItem.CALENDAR_THESIS
        AcademicActivity.THESIS_EXAM -> ColorItem.CALENDAR_THESIS
    }
