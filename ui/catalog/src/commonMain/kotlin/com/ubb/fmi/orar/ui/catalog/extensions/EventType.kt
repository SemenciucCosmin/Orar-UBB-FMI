package com.ubb.fmi.orar.ui.catalog.extensions

import androidx.compose.ui.graphics.Color
import com.ubb.fmi.orar.data.timetable.model.EventType
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

val EventType.colorLight: Color
    get() = when (this) {
        EventType.LECTURE -> Color(0xFF00A300)
        EventType.SEMINARY -> Color(0xFF5D3FD3)
        EventType.LABORATORY -> Color(0xFF0000D1)
        EventType.STAFF -> Color(0xFFD12300)
        EventType.PERSONAL -> Color(0xFFFFAC1C)
    }

val EventType.colorDark: Color
    get() = when (this) {
        EventType.LECTURE -> Color(0xFF1DB954)
        EventType.SEMINARY -> Color(0xFF8C6FF0)
        EventType.LABORATORY -> Color(0xFF3D5AFE)
        EventType.STAFF -> Color(0xFFFF6E40)
        EventType.PERSONAL -> Color(0xFFFFC000)
    }
