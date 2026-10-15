package com.ubb.fmi.orar.ui.catalog.extensions

import kotlinx.datetime.Month
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_april
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_august
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_december
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_february
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_january
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_july
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_june
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_march
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_may
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_november
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_october
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_september
import org.jetbrains.compose.resources.StringResource

val Month.labelRes: StringResource
    get() = when (this) {
        Month.JANUARY -> Res.string.lbl_january
        Month.FEBRUARY -> Res.string.lbl_february
        Month.MARCH -> Res.string.lbl_march
        Month.APRIL -> Res.string.lbl_april
        Month.MAY -> Res.string.lbl_may
        Month.JUNE -> Res.string.lbl_june
        Month.JULY -> Res.string.lbl_july
        Month.AUGUST -> Res.string.lbl_august
        Month.SEPTEMBER -> Res.string.lbl_september
        Month.OCTOBER -> Res.string.lbl_october
        Month.NOVEMBER -> Res.string.lbl_november
        Month.DECEMBER -> Res.string.lbl_december
    }
