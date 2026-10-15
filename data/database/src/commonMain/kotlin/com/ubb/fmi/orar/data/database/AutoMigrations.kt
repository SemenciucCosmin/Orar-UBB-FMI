package com.ubb.fmi.orar.data.database

import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.migration.AutoMigrationSpec

/**
 * Object containing all specific auto migration for database
 */
object AutoMigrations {
    @DeleteTable(tableName = "timetable_classes")
    class AutoMigrationSpec1To2 : AutoMigrationSpec

    @DeleteColumn(tableName = "study_lines", columnName = "degreeId")
    class AutoMigrationSpec5To6 : AutoMigrationSpec
}