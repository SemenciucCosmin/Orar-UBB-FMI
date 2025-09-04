package com.ubb.fmi.orar.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Entity for study line model
 * @param [id]: unique id
 * @param [name]: study line display name
 * @param [fieldId]: study line field id (Software Engineering - IS ...)
 * @param [levelId]: study line level id (Year1, Year2, Year3)
 * @param [degreeId]: study line degree id (License or Master)
 * @param [configurationId]: configuration id of which this study line belongs to
 */
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
