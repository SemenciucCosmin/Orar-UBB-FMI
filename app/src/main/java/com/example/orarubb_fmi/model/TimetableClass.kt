package com.example.orarubb_fmi.model

import com.example.orarubb_fmi.domain.model.ClassType
import com.example.orarubb_fmi.domain.model.Participant
import com.example.orarubb_fmi.domain.model.Week
import java.util.UUID

data class TimetableClass(
    val group: String,
    val day: String,
    val startHour: String,
    val endHour: String,
    val week: Week,
    val place: String,
    val participant: Participant,
    val classType: ClassType,
    val discipline: String,
    val professor: String
)
