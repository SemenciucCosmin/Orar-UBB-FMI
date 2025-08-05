package com.ubb.fmi.orar.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("study_lines")
data class StudyLineEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val studyYearId: String,
)
