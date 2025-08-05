package com.ubb.fmi.orar.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("teacher_classes")
data class TeacherClassEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val teacherId: String,
    val day: String,
    val hours: String,
    val frequencyId: String,
    val roomId: String,
    val studyLineId: String,
    val classTypeId: String,
    val subjectId: String,
    val subjectName: String,
)
