package com.ubb.fmi.orar.data.teachers.model

enum class TeacherTitle(
    val id: String,
    val orderIndex: Int,
) {
    PROFESSOR(
        id = "Prof.",
        orderIndex = 0
    ),
    SPEAKER(
        id = "Conf.",
        orderIndex = 1
    ),
    LECTURER(
        id = "Lect.",
        orderIndex = 2
    ),
    ASSISTANT(
        id = "Asist.",
        orderIndex = 3
    ),
    CANDIDATE(
        id = "Drd.",
        orderIndex = 4
    ),
    ASSOCIATE(
        id = "C.d.asociat",
        orderIndex = 5
    );

    companion object {
        fun getById(id: String): TeacherTitle {
            return entries.firstOrNull { it.id == id } ?: PROFESSOR
        }
    }
}
