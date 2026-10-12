package com.ubb.fmi.orar.ui.catalog.extensions

import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_laboratory
import orar_ubb_fmi.ui.catalog.generated.resources.ic_lecture
import orar_ubb_fmi.ui.catalog.generated.resources.ic_personal
import orar_ubb_fmi.ui.catalog.generated.resources.ic_seminary
import orar_ubb_fmi.ui.catalog.generated.resources.ic_staff
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_laboratory
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_lecture
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_personal
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_seminary
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_staff
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

val EventType.labelRes: StringResource
    get() = when (this) {
        EventType.LECTURE -> Res.string.lbl_lecture
        EventType.SEMINARY -> Res.string.lbl_seminary
        EventType.LABORATORY -> Res.string.lbl_laboratory
        EventType.STAFF -> Res.string.lbl_staff
        EventType.PERSONAL -> Res.string.lbl_personal
    }

val EventType.imageRes: DrawableResource
    get() = when (this) {
        EventType.LECTURE -> Res.drawable.ic_lecture
        EventType.SEMINARY -> Res.drawable.ic_seminary
        EventType.LABORATORY -> Res.drawable.ic_laboratory
        EventType.STAFF -> Res.drawable.ic_staff
        EventType.PERSONAL -> Res.drawable.ic_personal
    }

val EventType.colorItem: ColorItem
    get() = when (this) {
        EventType.LECTURE -> ColorItem.EVENT_LECTURE
        EventType.SEMINARY -> ColorItem.EVENT_SEMINARY
        EventType.LABORATORY -> ColorItem.EVENT_LABORATORY
        EventType.STAFF -> ColorItem.EVENT_STAFF
        EventType.PERSONAL -> ColorItem.EVENT_PERSONAL
    }
