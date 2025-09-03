package com.ubb.fmi.orar.ui.catalog.model

import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_all
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_assistant_abr
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_associate_abr
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_candidate_abr
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_lecturer_abr
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_professor_abr
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_speaker_abr
import org.jetbrains.compose.resources.StringResource

enum class TeacherTitleFilter(
    val id: String,
    val labelRes: StringResource,
    val orderIndex: Int,
) {
    ALL(
        id = "all",
        labelRes = Res.string.lbl_all,
        orderIndex = 0
    ),
    PROFESSOR(
        id = "Prof.",
        labelRes = Res.string.lbl_professor_abr,
        orderIndex = 1
    ),
    SPEAKER(
        id = "Conf.",
        labelRes = Res.string.lbl_speaker_abr,
        orderIndex = 2
    ),
    LECTURER(
        id = "Lect.",
        labelRes = Res.string.lbl_lecturer_abr,
        orderIndex = 3
    ),
    ASSISTANT(
        id = "Asist.",
        labelRes = Res.string.lbl_assistant_abr,
        orderIndex = 4
    ),
    CANDIDATE(
        id = "Drd.",
        labelRes = Res.string.lbl_candidate_abr,
        orderIndex = 5
    ),
    ASSOCIATE(
        id = "C.d.asociat",
        labelRes = Res.string.lbl_associate_abr,
        orderIndex = 6
    )
}