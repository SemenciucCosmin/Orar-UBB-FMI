package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.EventEntity

/**
 * Data Access Object (DAO) for managing timetable event entities in the database.
 * This interface provides methods to interact with the 'events' table,
 * including retrieving, inserting, and deleting timetable event records.
 */
@Dao
interface EventDao {

    /**
     * Get all timetable event entities by [configurationId] and [ownerId]
     */
    @Query("SELECT * FROM events WHERE configurationId LIKE :configurationId AND ownerId LIKE :ownerId")
    suspend fun getAllByConfigurationAndOwner(
        configurationId: String,
        ownerId: String,
    ): List<EventEntity>

    /**
     * Get timetable event entity [id]
     */
    @Query("SELECT * FROM events WHERE id LIKE :id ")
    suspend fun getById(id: String): EventEntity

    /**
     * Insert new timetable event [entity]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: EventEntity)

    /**
     * Delete all timetable event entities by [configurationId]
     */
    @Query("DELETE FROM events WHERE configurationId LIKE :configurationId")
    suspend fun deleteAll(configurationId: String)
}
