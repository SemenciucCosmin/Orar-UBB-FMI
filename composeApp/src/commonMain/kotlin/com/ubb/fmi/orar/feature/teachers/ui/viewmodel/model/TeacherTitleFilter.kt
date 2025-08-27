package com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model

import com.ubb.fmi.orar.data.teachers.model.TeacherTitle

enum class TeacherTitleFilter(
    val id: String,
    val label: String,
    val orderIndex: Int,
) {
    ALL(
        id = "all",
        label = "All",
        orderIndex = 0
    ),
    PROFESSOR(
        id = TeacherTitle.PROFESSOR.id,
        label = TeacherTitle.PROFESSOR.label,
        orderIndex = 1
    ),
    SPEAKER(
        id = TeacherTitle.SPEAKER.id,
        label = TeacherTitle.SPEAKER.label,
        orderIndex = 2
    ),
    LECTURER(
        id = TeacherTitle.LECTURER.id,
        label = TeacherTitle.LECTURER.label,
        orderIndex = 3
    ),
    ASSISTANT(
        id = TeacherTitle.ASSISTANT.id,
        label = TeacherTitle.ASSISTANT.label,
        orderIndex = 4
    ),
    CANDIDATE(
        id = TeacherTitle.CANDIDATE.id,
        label = TeacherTitle.CANDIDATE.label,
        orderIndex = 5
    ),
    ASSOCIATE(
        id = TeacherTitle.ASSOCIATE.id,
        label = TeacherTitle.ASSOCIATE.label,
        orderIndex = 6
    );

    companion object {
        fun getById(id: String?): TeacherTitleFilter {
            return entries.firstOrNull { it.id == id } ?: ALL
        }
    }
}