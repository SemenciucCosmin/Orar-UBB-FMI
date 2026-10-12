package com.ubb.fmi.orar.ui.catalog.extensions

import com.ubb.fmi.orar.domain.colorpack.model.ColorGroup
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem
import orar_ubb_fmi.ui.catalog.generated.resources.Res
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
    }

val ColorGroup.labelRes: StringResource
    get() = when (this) {
        ColorGroup.STATUS_INDICATOR -> Res.string.lbl_status_indicators
        ColorGroup.EVENT_TYPE -> Res.string.lbl_event_types
    }
