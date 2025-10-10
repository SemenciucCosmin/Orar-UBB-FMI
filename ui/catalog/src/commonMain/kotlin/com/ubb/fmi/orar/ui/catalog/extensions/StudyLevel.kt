package com.ubb.fmi.orar.ui.catalog.extensions

import com.ubb.fmi.orar.data.timetable.model.StudyLevel
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_level_1
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_level_2
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_level_3
import org.jetbrains.compose.resources.StringResource

val StudyLevel.labelRes: StringResource
    get() = when (this) {
        StudyLevel.LEVEL_1 -> Res.string.lbl_level_1
        StudyLevel.LEVEL_2 -> Res.string.lbl_level_2
        StudyLevel.LEVEL_3 -> Res.string.lbl_level_3
    }