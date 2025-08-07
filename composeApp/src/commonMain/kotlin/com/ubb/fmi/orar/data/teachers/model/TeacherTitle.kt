package com.ubb.fmi.orar.data.teachers.model

enum class TeacherTitle(val id: String) {
    PROFESSOR(id = "Prof."),
    SPEAKER(id = "Conf."),
    LECTURER(id = "Lect."),
    ASSISTANT(id = "Asist."),
    CANDIDATE(id = "Drd."),
    ASSOCIATE(id = "C.d.asociat");

    companion object {
        fun getById(id: String): TeacherTitle {
            return entries.firstOrNull { it.id == id } ?: PROFESSOR
        }
    }
}
