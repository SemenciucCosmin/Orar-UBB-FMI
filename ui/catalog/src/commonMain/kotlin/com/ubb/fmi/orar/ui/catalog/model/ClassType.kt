package com.ubb.fmi.orar.ui.catalog.model

import androidx.compose.ui.graphics.Color
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_laboratory
import orar_ubb_fmi.ui.catalog.generated.resources.ic_lecture
import orar_ubb_fmi.ui.catalog.generated.resources.ic_seminary
import orar_ubb_fmi.ui.catalog.generated.resources.ic_staff
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_laboratory
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_lecture
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_seminary
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_staff
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * Represents the type of a class in the timetable.
 *
 * @property id The unique identifier for the class type.
 * @property labelRes The string resource for the label of the class type.
 * @property colorLight The color associated with the class type for light theme.
 * @property colorDark The color associated with the class type for dark theme.
 * @property imageRes The drawable resource for the icon representing the class type.
 */
enum class ClassType(
    val id: String,
    val labelRes: StringResource,
    val colorLight: Color,
    val colorDark: Color,
    val imageRes: DrawableResource,
) {
    LECTURE(
        id = "Curs",
        labelRes = Res.string.lbl_lecture,
        colorLight = Color(0xFF00A300),
        colorDark = Color(0xF1DB954),
        imageRes = Res.drawable.ic_lecture,
    ),
    SEMINARY(
        id = "Seminar",
        labelRes = Res.string.lbl_seminary,
        colorLight = Color(0xFF5D3FD3),
        colorDark = Color(0xFF8C6FF0),
        imageRes = Res.drawable.ic_seminary,
    ),
    LABORATORY(
        id = "Laborator",
        labelRes = Res.string.lbl_laboratory,
        colorLight = Color(0xFF0000D1),
        colorDark = Color(0xFF3D5AFE),
        imageRes = Res.drawable.ic_laboratory,
    ),
    STAFF(
        id = "Colectiv",
        labelRes = Res.string.lbl_staff,
        colorLight = Color(0xFFD12300),
        colorDark = Color(0xFFFF6E40),
        imageRes = Res.drawable.ic_staff,
    );

    companion object {
        fun getById(id: String): ClassType {
            return entries.firstOrNull { it.id == id } ?: LECTURE
        }
    }
}