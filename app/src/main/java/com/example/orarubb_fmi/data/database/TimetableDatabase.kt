package com.example.orarubb_fmi.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.orarubb_fmi.data.datasource.dao.TimetableClassEntity
import com.example.orarubb_fmi.data.datasource.dao.TimetableDao
import com.example.orarubb_fmi.data.datasource.dao.TimetableInfoEntity

@Database(
    version = 1,
    entities = [
        TimetableInfoEntity::class,
        TimetableClassEntity::class
    ],
    exportSchema = false
)
abstract class TimetableDatabase : RoomDatabase() {
    abstract fun timetableDao(): TimetableDao
}
