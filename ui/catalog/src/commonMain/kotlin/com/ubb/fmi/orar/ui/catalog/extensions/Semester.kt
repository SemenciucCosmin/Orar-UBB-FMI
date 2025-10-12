package com.ubb.fmi.orar.ui.catalog.extensions

import com.ubb.fmi.orar.domain.timetable.model.Semester
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_semester_1
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_semester_2
import org.jetbrains.compose.resources.StringResource

val Semester.labelRes: StringResource
    get() = when (this) {
        Semester.FIRST -> Res.string.lbl_semester_1
        Semester.SECOND -> Res.string.lbl_semester_2
    }
