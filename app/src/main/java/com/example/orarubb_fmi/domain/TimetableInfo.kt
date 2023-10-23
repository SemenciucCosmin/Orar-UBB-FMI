package com.example.orarubb_fmi.domain

data class TimetableInfo(
    val year: Int,
    val semester: Int,
    val studyField: StudyField,
    val studyLanguage: StudyLanguage,
    val studyYear: Int
)
