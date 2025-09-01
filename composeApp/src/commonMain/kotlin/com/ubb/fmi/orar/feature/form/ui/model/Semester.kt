package com.ubb.fmi.orar.feature.form.ui.model

import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.lbl_semester_1
import orar_ubb_fmi.composeapp.generated.resources.lbl_semester_2
import org.jetbrains.compose.resources.StringResource

enum class Semester(
    val id: String,
    val labelRes: StringResource,
) {
    FIRST(
        id = "1",
        labelRes = Res.string.lbl_semester_1
    ),
    SECOND(
        id = "2",
        labelRes = Res.string.lbl_semester_2
    );

    companion object {
        fun getById(id: String): Semester {
            return entries.firstOrNull { it.id == id } ?: FIRST
        }
    }
}