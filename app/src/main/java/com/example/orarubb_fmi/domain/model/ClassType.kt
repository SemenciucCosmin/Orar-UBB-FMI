package com.example.orarubb_fmi.domain.model

enum class ClassType {
    COURSE,
    SEMINARY,
    LABORATORY;

    companion object {
        fun getClassType(classString: String): ClassType {
            return when (classString) {
                "Curs" -> COURSE
                "Seminar" -> SEMINARY
                else -> LABORATORY
            }
        }
    }
}
