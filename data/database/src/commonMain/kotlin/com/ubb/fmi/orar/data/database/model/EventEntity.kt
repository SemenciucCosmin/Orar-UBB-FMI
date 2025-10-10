package com.ubb.fmi.orar.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Entity for timetable event model
 * @param [id]: unique id
 * @param [day]: day on which the class takes place
 * @param [startHour]: hour at which the class starts
 * @param [endHour]: hour at which the class ends
 * @param [frequencyId]: frequency at which the class takes place (Week1, Week2 or both)
 * @param [studyLine]: study line to which the class belongs
 * @param [room]: room in which the class takes place
 * @param [participant]: participant to the class (a group, whole year ...)
 * @param [classType]: type of the class (Lecture, Seminary, Laboratory, Staff)
 * @param [ownerId]: owner of the class (a room, a teacher, a study line ...)
 * @param [subject]: subject which takes place during the class
 * @param [teacher]: teacher that hosts the class
 * @param [isVisible]: determines the visibility of this class on the users timetable
 * @param [configurationId]: configuration id of which this class belongs to
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
