package com.ubb.fmi.orar.ui.catalog.model

import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_both_weeks
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_week_1
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_week_2
import org.jetbrains.compose.resources.StringResource

enum class Frequency(
    val id: String,
    val labelRes: StringResource
) {
    WEEK_1(
        id = "sapt. 1",
        labelRes = Res.string.lbl_week_1,
    ),
    WEEK_2(
        id = "sapt. 2",
        labelRes = Res.string.lbl_week_2,
    ),
    BOTH(
        id = "null",
        labelRes = Res.string.lbl_both_weeks,
    );

    companion object {
        fun getById(id: String): Frequency {
            return entries.firstOrNull { it.id == id } ?: BOTH
        }
    }
}
