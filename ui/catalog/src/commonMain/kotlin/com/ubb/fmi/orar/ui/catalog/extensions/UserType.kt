package com.ubb.fmi.orar.ui.catalog.extensions

import com.ubb.fmi.orar.domain.usertimetable.model.UserType
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_student
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_teacher
import org.jetbrains.compose.resources.StringResource

val UserType.labelRes: StringResource
    get() = when (this) {
        UserType.STUDENT -> Res.string.lbl_student
        UserType.TEACHER -> Res.string.lbl_teacher
    }
