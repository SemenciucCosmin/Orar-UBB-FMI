package com.ubb.fmi.orar.ui.catalog.model

import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_level_1
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_level_2
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_level_3
import org.jetbrains.compose.resources.StringResource

/**
 * Represents the study levels in the academic program.
 *
 * @property id The unique identifier for the study level.
 * @property labelRes The string resource for the label of the study level.
 * @property notation The notation used to represent the study level.
 */
enum class StudyLevel(
    val id: String,
    val labelRes: StringResource,
    val notation: String,
) {
    LEVEL_1(
        id = "Anul 1",
        labelRes = Res.string.lbl_level_1,
        notation = "1"
    ),
    LEVEL_2(
        id = "Anul 2",
        labelRes = Res.string.lbl_level_2,
        notation = "2"
    ),
    LEVEL_3(
        id = "Anul 3",
        labelRes = Res.string.lbl_level_3,
        notation = "3"
    );

    companion object {
        fun getById(id: String): StudyLevel {
            return entries.firstOrNull { it.id == id } ?: LEVEL_1
        }
    }
}