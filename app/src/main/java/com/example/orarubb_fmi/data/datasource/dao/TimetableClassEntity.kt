package com.example.orarubb_fmi.data.datasource.dao

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "timetable_class",
    primaryKeys = ["id"]
)
data class TimetableClassEntity(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "group") val group: String,
    @ColumnInfo(name = "day") val day: String,
    @ColumnInfo(name = "startHour") val startHour: String,
    @ColumnInfo(name = "endHour") val endHour: String,
    @ColumnInfo(name = "week") val week: Int,
    @ColumnInfo(name = "place") val place: String,
    @ColumnInfo(name = "participant") val participant: Int,
    @ColumnInfo(name = "classType") val classType: Int,
    @ColumnInfo(name = "discipline") val discipline: String,
    @ColumnInfo(name = "professor") val professor: String
)
