package com.ubb.fmi.orar.ui.catalog.extensions

import com.ubb.fmi.orar.domain.colorpack.model.ColorGroup
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_academic_calendar
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_calendar_holiday
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_calendar_internship
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_calendar_preparation
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_calendar_session
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_calendar_teaching
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_calendar_thesis
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_color_ongoing
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_color_upcoming
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_event_types
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_laboratory
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_lecture
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_personal
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_seminary
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_staff
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_status_indicators
import org.jetbrains.compose.resources.StringResource

val ColorItem.labelRes: StringResource
    get() = when (this) {
        ColorItem.STATUS_ONGOING -> Res.string.lbl_color_ongoing
        ColorItem.STATUS_UPCOMING -> Res.string.lbl_color_upcoming
        ColorItem.EVENT_LECTURE -> Res.string.lbl_lecture
        ColorItem.EVENT_SEMINARY -> Res.string.lbl_seminary
        ColorItem.EVENT_LABORATORY -> Res.string.lbl_laboratory
        ColorItem.EVENT_STAFF -> Res.string.lbl_staff
        ColorItem.EVENT_PERSONAL -> Res.string.lbl_personal
        ColorItem.CALENDAR_PREPARATION -> Res.string.lbl_calendar_preparation
        ColorItem.CALENDAR_TEACHING -> Res.string.lbl_calendar_teaching
        ColorItem.CALENDAR_HOLIDAY -> Res.string.lbl_calendar_holiday
        ColorItem.CALENDAR_SESSION -> Res.string.lbl_calendar_session
        ColorItem.CALENDAR_INTERNSHIP -> Res.string.lbl_calendar_internship
        ColorItem.CALENDAR_THESIS -> Res.string.lbl_calendar_thesis
    }

val ColorGroup.labelRes: StringResource
    get() = when (this) {
        ColorGroup.STATUS_INDICATOR -> Res.string.lbl_status_indicators
        ColorGroup.EVENT_TYPE -> Res.string.lbl_event_types
        ColorGroup.ACADEMIC_CALENDAR -> Res.string.lbl_academic_calendar
    }
