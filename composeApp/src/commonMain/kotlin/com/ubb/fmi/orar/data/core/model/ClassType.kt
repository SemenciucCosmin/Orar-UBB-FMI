package com.ubb.fmi.orar.data.core.model

import androidx.compose.ui.graphics.Color
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_laboratory
import orar_ubb_fmi.composeapp.generated.resources.ic_lecture
import orar_ubb_fmi.composeapp.generated.resources.ic_seminary
import orar_ubb_fmi.composeapp.generated.resources.ic_staff
import org.jetbrains.compose.resources.DrawableResource

enum class ClassType(
    val id: String,
    val label: String,
    val color: Color,
    val imageRes: DrawableResource,
) {
    LECTURE(
        id = "Curs",
        label = "Curs",
        color = Color(0xFF00A300),
        imageRes = Res.drawable.ic_lecture,
    ),
    SEMINARY(
        id = "Seminar",
        label = "Seminar",
        color = Color(0xFF5D3FD3),
        imageRes = Res.drawable.ic_seminary,
    ),
    LABORATORY(
        id = "Laborator",
        label = "Laborator",
        color = Color(0xFF0000D1),
        imageRes = Res.drawable.ic_laboratory,
    ),
    STAFF (
        id = "Colectiv",
        label = "Colectiv",
        color = Color(0xFFD12300),
        imageRes = Res.drawable.ic_staff,
    );

    companion object {
        fun getById(id: String): ClassType {
            return entries.firstOrNull { it.id == id } ?: LECTURE
        }
    }
}
