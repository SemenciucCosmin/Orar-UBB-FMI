package com.ubb.fmi.orar.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Entity for timetable event model
 * @param [id]: unique id
 * @param [configurationId]: configuration id of which this event belongs to
 * @param [ownerId]: owner of the event (a room, a teacher, a study line ...)
 * @param [dayId]: day on which the event takes place
 * @param [frequencyId]: frequency at which the event takes place (Week1, Week2 or both)
 * @param [startHour]: hour at which the event starts
 * @param [endHour]: hour at which the event ends
 * @param [locationId]: id of location in which the events takes place
 * @param [locationName]: name of location in which the events takes place
 * @param [locationAddress]: address of location in which the events takes place
 * @param [activityId]: id of activity during event
 * @param [activityName]: name of activity during event
 * @param [participantId]: id of participant at event
 * @param [participantName]: name of participant at event
 * @param [hostId]: id of host at event
 * @param [hostName]: name of host at event
 * @param [isVisible]: determines the visibility of this event on the users timetable
 */
@Entity(
    tableName = "events",
    primaryKeys = ["id", "configurationId"]
)
data class EventEntity(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "configurationId") val configurationId: String,
    @ColumnInfo(name = "ownerId") val ownerId: String,
    @ColumnInfo(name = "dayId") val dayId: String,
    @ColumnInfo(name = "frequencyId") val frequencyId: String,
    @ColumnInfo(name = "startHour") val startHour: Int,
    @ColumnInfo(name = "endHour") val endHour: Int,
    @ColumnInfo(name = "locationId") val locationId: String?,
    @ColumnInfo(name = "locationName") val locationName: String?,
    @ColumnInfo(name = "locationAddress") val locationAddress: String?,
    @ColumnInfo(name = "activityId") val activityId: String,
    @ColumnInfo(name = "activityName") val activityName: String,
    @ColumnInfo(name = "typeId") val typeId: String,
    @ColumnInfo(name = "participantId") val participantId: String?,
    @ColumnInfo(name = "participantName") val participantName: String?,
    @ColumnInfo(name = "hostId") val hostId: String?,
    @ColumnInfo(name = "hostName") val hostName: String?,
    @ColumnInfo(name = "isVisible") val isVisible: Boolean,
)
