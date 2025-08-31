package com.ubb.fmi.orar.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("subjects")
data class SubjectEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val configurationId: String,
    val name: String,
)
