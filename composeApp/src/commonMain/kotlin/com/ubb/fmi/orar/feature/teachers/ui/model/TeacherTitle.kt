package com.ubb.fmi.orar.feature.teachers.ui.model

import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.lbl_assistant_abr
import orar_ubb_fmi.composeapp.generated.resources.lbl_associate_abr
import orar_ubb_fmi.composeapp.generated.resources.lbl_candidate_abr
import orar_ubb_fmi.composeapp.generated.resources.lbl_lecturer_abr
import orar_ubb_fmi.composeapp.generated.resources.lbl_professor_abr
import orar_ubb_fmi.composeapp.generated.resources.lbl_speaker_abr
import org.jetbrains.compose.resources.StringResource

enum class TeacherTitle(
    val id: String,
    val labelRes: StringResource,
) {
    PROFESSOR(
        id = "Prof.",
        labelRes = Res.string.lbl_professor_abr,
    ),
    SPEAKER(
        id = "Conf.",
        labelRes = Res.string.lbl_speaker_abr,
    ),
    LECTURER(
        id = "Lect.",
        labelRes = Res.string.lbl_lecturer_abr,
    ),
    ASSISTANT(
        id = "Asist.",
        labelRes = Res.string.lbl_assistant_abr,
    ),
    CANDIDATE(
        id = "Drd.",
        labelRes = Res.string.lbl_candidate_abr,
    ),
    ASSOCIATE(
        id = "C.d.asociat",
        labelRes = Res.string.lbl_associate_abr,
    );

    companion object {
        fun getById(id: String): TeacherTitle {
            return entries.firstOrNull { it.id == id } ?: PROFESSOR
        }
    }
}
