package com.ubb.fmi.orar.data.core.model

enum class Participant(val id: String) {
    SEMIGROUP_1("/1"),
    SEMIGROUP_2("/2"),
    WHOLE_GROUP("whole_group"),
    WHOLE_YEAR("whole_year");

    companion object {
        fun getById(id: String): Participant {
            return entries.firstOrNull { it.id == id } ?: SEMIGROUP_1
        }
    }
}
