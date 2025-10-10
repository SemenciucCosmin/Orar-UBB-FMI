package com.ubb.fmi.orar.data.timetable.model

/**
 * Enum class for all teacher title types
 * @param [id]: unique id of the enum
 * @param [label]: label
 * @param [orderIndex]: index by which teacher titles need to be ordered
 */
enum class TeacherTitle(
    val id: String,
    val label: String,
    val orderIndex: Int,
) {
    PROFESSOR(
        id = "Prof.",
        label = "Prof.",
        orderIndex = 0
    ),
    SPEAKER(
        id = "Conf.",
        label = "Conf.",
        orderIndex = 1
    ),
    LECTURER(
        id = "Lect.",
        label = "Lect.",
        orderIndex = 2
    ),
    ASSISTANT(
        id = "Asist.",
        label = "Asist.",
        orderIndex = 3
    ),
    CANDIDATE(
        id = "Drd.",
        label = "Drd.",
        orderIndex = 4
    ),
    ASSOCIATE(
        id = "C.d.asociat",
        label = "C.d.asociat",
        orderIndex = 5
    );

    companion object {
        fun getById(id: String): TeacherTitle {
            return entries.firstOrNull { it.id == id } ?: PROFESSOR
        }
    }
}
