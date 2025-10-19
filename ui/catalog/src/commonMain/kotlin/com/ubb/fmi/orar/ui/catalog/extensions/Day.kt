package com.ubb.fmi.orar.ui.catalog.extensions

import com.ubb.fmi.orar.data.timetable.model.Day
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_friday
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_friday_short
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_monday
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_monday_short
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_saturday
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_saturday_short
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_sunday
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_sunday_short
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_thursday
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_thursday_short
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_tuesday
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_tuesday_short
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_wednesday
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_wednesday_short
import org.jetbrains.compose.resources.StringResource

val Day.labelRes: StringResource
    get() = when (this) {
        Day.MONDAY -> Res.string.lbl_monday
        Day.TUESDAY -> Res.string.lbl_tuesday
        Day.WEDNESDAY -> Res.string.lbl_wednesday
        Day.THURSDAY -> Res.string.lbl_thursday
        Day.FRIDAY -> Res.string.lbl_friday
        Day.SATURDAY -> Res.string.lbl_saturday
        Day.SUNDAY -> Res.string.lbl_sunday
    }

val Day.shortLabelRes: StringResource
    get() = when (this) {
        Day.MONDAY -> Res.string.lbl_monday_short
        Day.TUESDAY -> Res.string.lbl_tuesday_short
        Day.WEDNESDAY -> Res.string.lbl_wednesday_short
        Day.THURSDAY -> Res.string.lbl_thursday_short
        Day.FRIDAY -> Res.string.lbl_friday_short
        Day.SATURDAY -> Res.string.lbl_saturday_short
        Day.SUNDAY -> Res.string.lbl_sunday_short
    }
