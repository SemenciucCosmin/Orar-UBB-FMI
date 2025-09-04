package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.TimetableClassEntity

/**
 * Data Access Object (DAO) for managing timetable class entities in the database.
 * This interface provides methods to interact with the 'timetable_classes' table,
 * including retrieving, inserting, and deleting timetable class records.
 */
@Dao
interface TimetableClassDao {

    /**
     * Get all timetable class entities by [configurationId] and [ownerId]
     */
    @Query("SELECT * FROM timetable_classes WHERE configurationId LIKE :configurationId AND ownerId LIKE :ownerId")
    suspend fun getAllByConfigurationAndOwner(
        configurationId: String,
        ownerId: String,
    ): List<TimetableClassEntity>

    /**
     * Get timetable class entity [id]
     */
    @Query("SELECT * FROM timetable_classes WHERE id LIKE :id ")
    suspend fun getById(id: String): TimetableClassEntity

    /**
     * Insert new timetable class [entity]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TimetableClassEntity)

    /**
     * Delete all timetable class entities by [configurationId]
     */
    @Query("DELETE FROM timetable_classes WHERE configurationId LIKE :configurationId")
    suspend fun deleteAll(configurationId: String)
}
