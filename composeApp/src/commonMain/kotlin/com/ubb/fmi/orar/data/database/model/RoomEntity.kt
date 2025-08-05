package com.ubb.fmi.orar.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("rooms")
data class RoomEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val location: String,
)
