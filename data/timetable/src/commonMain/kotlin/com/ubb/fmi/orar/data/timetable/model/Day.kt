package com.ubb.fmi.orar.data.timetable.model

/**
 * Enum class for all days of the week found on each timetable class
 * @param [id]: unique id found on each timetable class
 * @param [orderIndex]: index by which instances have to be sorted
 */
enum class Day(
    val id: String,
    val orderIndex: Int
) {
    MONDAY(
        id = "Luni",
        orderIndex = 0
    ),
    TUESDAY(
        id = "Marti",
        orderIndex = 1
    ),
    WEDNESDAY(
        id = "Miercuri",
        orderIndex = 2
    ),
    THURSDAY(
        id = "Joi",
        orderIndex = 3
    ),
    FRIDAY(
        id = "Vineri",
        orderIndex = 4
    ),
    SATURDAY(
        id = "Sambata",
        orderIndex = 5
    ),
    SUNDAY(
        id = "Duminica",
        orderIndex = 6
    );

    companion object {
        fun getById(id: String): Day {
            return entries.firstOrNull { it.id == id } ?: MONDAY
        }
    }
}