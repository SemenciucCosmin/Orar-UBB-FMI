package com.ubb.fmi.orar.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("subject_classes")
data class SubjectClassEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val subjectId: String,
    val day: String,
    val hours: String,
    val frequencyId: String,
    val roomId: String,
    val studyLineId: String,
    val classTypeId: String,
    val teacherId: String,
)
