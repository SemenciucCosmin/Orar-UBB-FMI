package com.ubb.fmi.orar.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("study_line_classes")
data class StudyLineClassEntity(
    @PrimaryKey val id: String,
    val studyLineId: String,
    val groupId: String,
    val day: String,
    val startHour: String,
    val endHour: String,
    val frequencyId: String,
    val roomId: String,
    val participantId: String,
    val participantName: String,
    val classTypeId: String,
    val subjectId: String,
    val teacherId: String,
    val isVisible: Boolean,
)
