package com.example.orarubb_fmi.model

import com.example.orarubb_fmi.domain.model.StudyField
import com.example.orarubb_fmi.domain.model.StudyLanguage

data class TimetableInfo(
    val year: String,
    val semester: String,
    val studyField: StudyField,
    val studyLanguage: StudyLanguage,
    val studyYear: String
)
