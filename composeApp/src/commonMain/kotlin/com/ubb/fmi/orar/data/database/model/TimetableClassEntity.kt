package com.ubb.fmi.orar.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("timetable_classes")
data class TimetableClassEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val day: String,
    val startHour: String,
    val endHour: String,
    val frequencyId: String,
    val studyLine: String,
    val room: String,
    val participant: String,
    val classType: String,
    val ownerId: String,
    val groupId: String,
    val ownerTypeId: String,
    val subject: String,
    val teacher: String,
    val isVisible: Boolean,
    val configurationId: String,
)
