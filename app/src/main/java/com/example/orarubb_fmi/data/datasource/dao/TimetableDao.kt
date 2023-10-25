package com.example.orarubb_fmi.data.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TimetableDao {

    @Query("SELECT * FROM timetable_info")
    suspend fun getTimetableInfo(): TimetableInfoEntity

    @Query("SELECT * FROM timetable_class")
    suspend fun getTimetableClasses(): List<TimetableClassEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimetableInfo(timetableInfoEntity: TimetableInfoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimetableClass(timetableClassEntity: TimetableClassEntity)
}
