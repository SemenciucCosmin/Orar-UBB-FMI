package com.ubb.fmi.orar.data.database

import androidx.room.DeleteTable
import androidx.room.migration.AutoMigrationSpec

object AutoMigrations {
    @DeleteTable(tableName = "timetable_classes")
    class AutoMigrationSpec1To2 : AutoMigrationSpec
}