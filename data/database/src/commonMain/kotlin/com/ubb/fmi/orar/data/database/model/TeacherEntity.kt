package com.ubb.fmi.orar.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "teachers",
    primaryKeys = ["id", "configurationId"]
)
data class TeacherEntity(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "titleId") val titleId: String,
    @ColumnInfo(name = "configurationId") val configurationId: String,
)
