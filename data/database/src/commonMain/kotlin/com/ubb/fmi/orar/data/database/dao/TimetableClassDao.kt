package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.TimetableClassEntity

@Dao
interface TimetableClassDao {

    @Query("SELECT * FROM timetable_classes WHERE configurationId LIKE :configurationId AND ownerId LIKE :ownerId")
    suspend fun getAllByConfigurationAndOwner(
        configurationId: String,
        ownerId: String,
    ): List<TimetableClassEntity>

    @Query("SELECT * FROM timetable_classes WHERE id LIKE :timetableClassId ")
    suspend fun getById(timetableClassId: String): TimetableClassEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(timetableClassEntity: TimetableClassEntity)

    @Query("DELETE FROM timetable_classes WHERE configurationId LIKE :configurationId")
    suspend fun deleteAll(configurationId: String)
}
