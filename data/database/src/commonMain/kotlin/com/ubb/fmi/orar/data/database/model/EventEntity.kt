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
 * @param [location]: location in which the events takes place
 * @param [activity]: activity during event
 * @param [participant]: participant at event
 * @param [caption]: short details of event
 * @param [details]: details of an event
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
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "activity") val activity: String,
    @ColumnInfo(name = "typeId") val typeId: String,
    @ColumnInfo(name = "participant") val participant: String,
    @ColumnInfo(name = "caption") val caption: String,
    @ColumnInfo(name = "details") val details: String,
    @ColumnInfo(name = "isVisible") val isVisible: Boolean,
)
