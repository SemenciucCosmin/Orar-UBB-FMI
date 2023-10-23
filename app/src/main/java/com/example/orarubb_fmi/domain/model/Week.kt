package com.example.orarubb_fmi.domain.model

enum class Week {
    WEEK_1,
    WEEK_2,
    BOTH;

    companion object {
        fun getWeekType(week: String): Week {
            return when (week) {
                "sapt. 1" -> WEEK_1
                "sapt. 2" -> WEEK_2
                else -> BOTH
            }
        }
    }
}
