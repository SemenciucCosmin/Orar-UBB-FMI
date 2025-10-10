package com.ubb.fmi.orar.ui.catalog.extensions

import com.ubb.fmi.orar.data.timetable.model.Frequency
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_both_weeks
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_week_1
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_week_2
import org.jetbrains.compose.resources.StringResource

val Frequency.labelRes: StringResource
    get() = when (this) {
        Frequency.WEEK_1 -> Res.string.lbl_week_1
        Frequency.WEEK_2 -> Res.string.lbl_week_2
        Frequency.BOTH -> Res.string.lbl_both_weeks
    }
