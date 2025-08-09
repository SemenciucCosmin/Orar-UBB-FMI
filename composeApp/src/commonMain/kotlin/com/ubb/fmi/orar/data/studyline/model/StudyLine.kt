package com.ubb.fmi.orar.data.studyline.model

data class StudyLine(
    val id: String,
    val name: String,
    val studyYearsIds: List<String>,
    val degreeId: String,
)
