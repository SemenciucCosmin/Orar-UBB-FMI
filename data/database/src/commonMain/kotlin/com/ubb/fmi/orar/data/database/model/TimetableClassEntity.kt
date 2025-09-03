package com.ubb.fmi.orar.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

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
    @ColumnInfo(name = "groupId") val groupId: String,
    @ColumnInfo(name = "ownerTypeId") val ownerTypeId: String,
    @ColumnInfo(name = "subject") val subject: String,
    @ColumnInfo(name = "teacher") val teacher: String,
    @ColumnInfo(name = "isVisible") val isVisible: Boolean,
    @ColumnInfo(name = "configurationId") val configurationId: String,
)
