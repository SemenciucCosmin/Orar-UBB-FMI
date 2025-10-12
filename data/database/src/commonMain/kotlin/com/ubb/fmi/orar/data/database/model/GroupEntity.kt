package com.ubb.fmi.orar.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Entity for group model
 * @param [id]: unique id
 * @param [name]: group display name
 * @param [studyLineId]: study line id to with the groups belongs to
 * @param [configurationId]: configuration id of which this group belongs to
 */
@Entity(
    tableName = "groups",
    primaryKeys = ["id", "configurationId"]
)
data class GroupEntity(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "studyLineId") val studyLineId: String,
    @ColumnInfo(name = "configurationId") val configurationId: String,
)
