package com.ubb.fmi.orar.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Entity for subject model
 * @param [id]: unique id
 * @param [name]: subject display name
 * @param [configurationId]: configuration id of which this subject belongs to
 */
@Entity(
    tableName = "subjects",
    primaryKeys = ["id", "configurationId"]
)
data class SubjectEntity(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "configurationId") val configurationId: String,
)
