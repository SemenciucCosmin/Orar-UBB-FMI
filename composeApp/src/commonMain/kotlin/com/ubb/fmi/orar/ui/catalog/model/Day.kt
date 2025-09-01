package com.ubb.fmi.orar.ui.catalog.model

import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.lbl_friday
import orar_ubb_fmi.composeapp.generated.resources.lbl_monday
import orar_ubb_fmi.composeapp.generated.resources.lbl_saturday
import orar_ubb_fmi.composeapp.generated.resources.lbl_sunday
import orar_ubb_fmi.composeapp.generated.resources.lbl_thursday
import orar_ubb_fmi.composeapp.generated.resources.lbl_tuesday
import orar_ubb_fmi.composeapp.generated.resources.lbl_wednesday
import org.jetbrains.compose.resources.StringResource

enum class Day(
    val id: String,
    val labelRes: StringResource,
    val orderIndex: Int
) {
    MONDAY(
        id = "Luni",
        labelRes = Res.string.lbl_monday,
        orderIndex = 0
    ),
    TUESDAY(
        id = "Marti",
        labelRes = Res.string.lbl_tuesday,
        orderIndex = 1
    ),
    WEDNESDAY(
        id = "Miercuri",
        labelRes = Res.string.lbl_wednesday,
        orderIndex = 2
    ),
    THURSDAY(
        id = "Joi",
        labelRes = Res.string.lbl_thursday,
        orderIndex = 3
    ),
    FRIDAY(
        id = "Vineri",
        labelRes = Res.string.lbl_friday,
        orderIndex = 4
    ),
    SATURDAY(
        id = "Sambata",
        labelRes = Res.string.lbl_saturday,
        orderIndex = 5
    ),
    SUNDAY(
        id = "Duminica",
        labelRes = Res.string.lbl_sunday,
        orderIndex = 6
    );

    companion object {
        fun getById(id: String): Day {
            return entries.firstOrNull { it.id == id } ?: MONDAY
        }
    }
}