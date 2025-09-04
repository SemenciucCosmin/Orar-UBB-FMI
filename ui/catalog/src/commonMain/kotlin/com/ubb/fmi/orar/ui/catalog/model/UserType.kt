package com.ubb.fmi.orar.ui.catalog.model

import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_student
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_teacher
import org.jetbrains.compose.resources.StringResource

/**
 * Represents the user types in the application.
 *
 * @property id The unique identifier for the user type.
 * @property labelRes The string resource for the label of the user type.
 */
enum class UserType(
    val id: String,
    val labelRes: StringResource
) {
    STUDENT(
        id = "student",
        labelRes = Res.string.lbl_student
    ),
    TEACHER(
        id = "teacher",
        labelRes = Res.string.lbl_teacher
    );

    companion object {
        fun getById(id: String): UserType {
            return entries.firstOrNull { it.id == id } ?: STUDENT
        }
    }
}