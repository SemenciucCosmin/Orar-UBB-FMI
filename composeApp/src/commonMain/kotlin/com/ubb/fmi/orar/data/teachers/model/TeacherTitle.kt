package com.ubb.fmi.orar.data.teachers.model

enum class TeacherTitle(
    val id: String,
    val label: String
) {
    PROFESSOR(
        id = "Prof.",
        label = "Prof.",
    ),
    SPEAKER(
        id = "Conf.",
        label = "Conf.",
    ),
    LECTURER(
        id = "Lect.",
        label = "Lect.",
    ),
    ASSISTANT(
        id = "Asist.",
        label = "Asist.",
    ),
    CANDIDATE(
        id = "Drd.",
        label = "Drd.",
    ),
    ASSOCIATE(
        id = "C.d.asociat",
        label = "C.d.asociat",
    );

    companion object {
        fun getById(id: String): TeacherTitle {
            return entries.firstOrNull { it.id == id } ?: PROFESSOR
        }
    }
}
