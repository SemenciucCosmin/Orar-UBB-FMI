package com.ubb.fmi.orar.feature.calendar.ui.extensions

import com.ubb.fmi.orar.data.timetable.model.Week
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_week_1_abr
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_week_2_abr
import org.jetbrains.compose.resources.StringResource

/**
 * The number displayed for the week parity: 1 for odd weeks and 2 for even weeks.
 */
val Week.labelRes: StringResource
    get() = when (this) {
        Week.ODD -> Res.string.lbl_week_1_abr
        Week.EVEN -> Res.string.lbl_week_2_abr
    }
