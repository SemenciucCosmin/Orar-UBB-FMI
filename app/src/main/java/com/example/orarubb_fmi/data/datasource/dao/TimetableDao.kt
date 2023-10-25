package com.example.orarubb_fmi.data.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface TimetableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimetableInfo(timetableInfoEntity: TimetableInfoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimetableClass(timetableClassEntity: TimetableClassEntity)
}
