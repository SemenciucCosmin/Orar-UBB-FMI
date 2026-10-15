package com.ubb.fmi.orar.feature.calendar.ui.extensions

import com.ubb.fmi.orar.data.calendar.model.AcademicActivity
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_academic_preparation
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_christmas_holiday
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_easter_holiday
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_exam_session
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_holiday
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_internship_period
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_retake_session
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_teaching_activity
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_thesis_exam
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_thesis_preparation
import org.jetbrains.compose.resources.StringResource

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

val AcademicActivity.labelRes: StringResource
    get() = when (this) {
        AcademicActivity.ACADEMIC_PREPARATION -> Res.string.lbl_academic_preparation
        AcademicActivity.TEACHING_ACTIVITY -> Res.string.lbl_teaching_activity
        AcademicActivity.CHRISTMAS_HOLIDAY -> Res.string.lbl_christmas_holiday
        AcademicActivity.EASTER_HOLIDAY -> Res.string.lbl_easter_holiday
        AcademicActivity.EXAM_SESSION -> Res.string.lbl_exam_session
        AcademicActivity.RETAKE_SESSION -> Res.string.lbl_retake_session
        AcademicActivity.INTERNSHIP_PERIOD -> Res.string.lbl_internship_period
        AcademicActivity.THESIS_PREPARATION -> Res.string.lbl_thesis_preparation
        AcademicActivity.THESIS_EXAM -> Res.string.lbl_thesis_exam
        AcademicActivity.HOLIDAY -> Res.string.lbl_holiday
    }
