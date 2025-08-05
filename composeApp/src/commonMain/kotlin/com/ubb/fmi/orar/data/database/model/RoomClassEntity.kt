package com.ubb.fmi.orar.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("room_classes")
data class RoomClassEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val roomId: String,
    val day: String,
    val hours: String,
    val frequencyId: String,
    val studyLineId: String,
    val classTypeId: String,
    val subjectId: String,
    val teacherId: String,
)
