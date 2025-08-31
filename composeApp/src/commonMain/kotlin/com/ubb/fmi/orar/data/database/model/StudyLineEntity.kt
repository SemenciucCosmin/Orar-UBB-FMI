package com.ubb.fmi.orar.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "study_lines",
    primaryKeys = ["id", "configurationId"]
)
data class StudyLineEntity(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "fieldId") val fieldId: String,
    @ColumnInfo(name = "levelId") val levelId: String,
    @ColumnInfo(name = "degreeId") val degreeId: String,
    @ColumnInfo(name = "configurationId") val configurationId: String,
)
