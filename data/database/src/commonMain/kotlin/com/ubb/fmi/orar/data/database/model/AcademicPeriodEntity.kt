package com.ubb.fmi.orar.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Entity for academic calendar model, stored flat as one row per academic period
 * @param [startYear]: academic year start year
 * @param [endYear]: academic year-end year
 * @param [semesterId]: semester id of which this period belongs to
 * @param [isFinal]: whether the semester is for final year students
 * @param [studyLanguageIds]: comma separated study language ids the semester applies to
 * @param [activityId]: academic activity id during this period
 * @param [startDateMillis]: period start date in millis
 * @param [endDateMillis]: period end date in millis
 */
@Entity(
    tableName = "academic_periods",
    primaryKeys = ["startYear", "semesterId", "isFinal", "studyLanguageIds", "startDateMillis"]
)
data class AcademicPeriodEntity(
    @ColumnInfo(name = "startYear") val startYear: Int,
    @ColumnInfo(name = "endYear") val endYear: Int,
    @ColumnInfo(name = "semesterId") val semesterId: String,
    @ColumnInfo(name = "isFinal") val isFinal: Boolean,
    @ColumnInfo(name = "studyLanguageIds") val studyLanguageIds: String,
    @ColumnInfo(name = "activityId") val activityId: String,
    @ColumnInfo(name = "startDateMillis") val startDateMillis: Long,
    @ColumnInfo(name = "endDateMillis") val endDateMillis: Long,
)
