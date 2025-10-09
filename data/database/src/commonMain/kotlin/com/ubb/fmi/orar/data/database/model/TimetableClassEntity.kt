package com.ubb.fmi.orar.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Entity for timetable class model
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
    tableName = "timetable_classes",
    primaryKeys = ["id", "configurationId"]
)
data class TimetableClassEntity(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "day") val day: String,
    @ColumnInfo(name = "startHour") val startHour: String,
    @ColumnInfo(name = "endHour") val endHour: String,
    @ColumnInfo(name = "frequencyId") val frequencyId: String,
    @ColumnInfo(name = "studyLine") val studyLine: String,
    @ColumnInfo(name = "room") val room: String,
    @ColumnInfo(name = "participant") val participant: String,
    @ColumnInfo(name = "classType") val classType: String,
    @ColumnInfo(name = "ownerId") val ownerId: String,
    @ColumnInfo(name = "subject") val subject: String,
    @ColumnInfo(name = "teacher") val teacher: String,
    @ColumnInfo(name = "isVisible") val isVisible: Boolean,
    @ColumnInfo(name = "configurationId") val configurationId: String,
)
