package com.example.orarubb_fmi.model

import com.example.orarubb_fmi.domain.model.StudyField
import com.example.orarubb_fmi.domain.model.StudyLanguage

data class TimetableInfo(
    val year: Int,
    val semester: Int,
    val studyField: StudyField,
    val studyLanguage: StudyLanguage,
    val studyYear: Int
)
