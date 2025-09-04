package com.ubb.fmi.orar.ui.catalog.model

import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_friday
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_monday
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_saturday
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_sunday
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_thursday
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_tuesday
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_wednesday
import org.jetbrains.compose.resources.StringResource

/**
 * Represents the days of the week in the timetable.
 *
 * @property id The unique identifier for the day.
 * @property labelRes The string resource for the label of the day.
 */
enum class Day(
    val id: String,
    val labelRes: StringResource,
) {
    MONDAY(
        id = "Luni",
        labelRes = Res.string.lbl_monday,
    ),
    TUESDAY(
        id = "Marti",
        labelRes = Res.string.lbl_tuesday,
    ),
    WEDNESDAY(
        id = "Miercuri",
        labelRes = Res.string.lbl_wednesday,
    ),
    THURSDAY(
        id = "Joi",
        labelRes = Res.string.lbl_thursday,
    ),
    FRIDAY(
        id = "Vineri",
        labelRes = Res.string.lbl_friday,
    ),
    SATURDAY(
        id = "Sambata",
        labelRes = Res.string.lbl_saturday,
    ),
    SUNDAY(
        id = "Duminica",
        labelRes = Res.string.lbl_sunday,
    );

    companion object {
        fun getById(id: String): Day {
            return entries.firstOrNull { it.id == id } ?: MONDAY
        }
    }
}