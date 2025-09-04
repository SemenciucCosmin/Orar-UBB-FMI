package com.ubb.fmi.orar.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Entity for room model
 * @param [id]: unique id
 * @param [name]: room display name
 * @param [location]: room location
 * @param [configurationId]: configuration id of which this room belongs to
 */
@Entity(
    tableName = "rooms",
    primaryKeys = ["id", "configurationId"]
)
data class RoomEntity(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "configurationId") val configurationId: String,
)
