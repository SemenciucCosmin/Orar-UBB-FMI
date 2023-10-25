package com.example.orarubb_fmi.model

import java.util.UUID

data class Timetable(
    val info: TimetableInfo,
    val classes: List<TimetableClass>
)
