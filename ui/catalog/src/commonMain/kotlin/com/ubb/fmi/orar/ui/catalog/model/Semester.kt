package com.ubb.fmi.orar.ui.catalog.model

import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_semester_1
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_semester_2
import org.jetbrains.compose.resources.StringResource

/**
 * Represents the semesters in the academic calendar.
 *
 * @property id The unique identifier for the semester.
 * @property labelRes The string resource for the label of the semester.
 */
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