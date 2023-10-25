package com.example.orarubb_fmi.data.datasource.dao

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "timetable_info",
    primaryKeys = ["id"]
)
data class TimetableInfoEntity(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "year") val year: String,
    @ColumnInfo(name = "semester") val semester: String,
    @ColumnInfo(name = "studyField") val studyField: Int,
    @ColumnInfo(name = "studyLanguage") val studyLanguage: Int,
    @ColumnInfo(name = "studyYear") val studyYear: String
)
