package com.ubb.fmi.orar.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Entity for teacher model
 * @param [id]: unique id
 * @param [name]: teacher display name
 * @param [titleId]: teacher title id (Prof., Lect. ...)
 * @param [configurationId]: configuration id of which this teacher belongs to
 */
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
