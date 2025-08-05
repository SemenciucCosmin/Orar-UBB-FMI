package com.ubb.fmi.orar.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("teachers")
data class TeacherEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
)
