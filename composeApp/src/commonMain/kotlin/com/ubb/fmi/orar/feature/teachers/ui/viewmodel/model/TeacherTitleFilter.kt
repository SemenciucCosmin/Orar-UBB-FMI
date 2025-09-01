package com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model

import com.ubb.fmi.orar.feature.teachers.ui.model.TeacherTitle
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.lbl_all
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
        id = TeacherTitle.PROFESSOR.id,
        labelRes = TeacherTitle.PROFESSOR.labelRes,
        orderIndex = 1
    ),
    SPEAKER(
        id = TeacherTitle.SPEAKER.id,
        labelRes = TeacherTitle.SPEAKER.labelRes,
        orderIndex = 2
    ),
    LECTURER(
        id = TeacherTitle.LECTURER.id,
        labelRes = TeacherTitle.LECTURER.labelRes,
        orderIndex = 3
    ),
    ASSISTANT(
        id = TeacherTitle.ASSISTANT.id,
        labelRes = TeacherTitle.ASSISTANT.labelRes,
        orderIndex = 4
    ),
    CANDIDATE(
        id = TeacherTitle.CANDIDATE.id,
        labelRes = TeacherTitle.CANDIDATE.labelRes,
        orderIndex = 5
    ),
    ASSOCIATE(
        id = TeacherTitle.ASSOCIATE.id,
        labelRes = TeacherTitle.ASSOCIATE.labelRes,
        orderIndex = 6
    );
}