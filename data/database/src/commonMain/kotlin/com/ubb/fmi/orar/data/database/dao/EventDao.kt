package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ubb.fmi.orar.data.database.model.EventEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing timetable event entities in the database.
 */
@Dao
interface EventDao {

    /**
     * Get all timetable event entities by [configurationId]
     */
    @Query("SELECT * FROM events WHERE configurationId LIKE :configurationId")
    fun getAllAsFlowByConfiguration(
        configurationId: String,
    ): Flow<List<EventEntity>>

    /**
     * Get all timetable event entities by [configurationId] and [ownerId]
     */
    @Query("SELECT * FROM events WHERE configurationId LIKE :configurationId AND ownerId LIKE :ownerId")
    fun getAllAsFlowByConfigurationAndOwner(
        configurationId: String,
        ownerId: String,
    ): Flow<List<EventEntity>>

    /**
     * Get all timetable event entities by [configurationId] and [ownerId]
     */
    @Query("SELECT * FROM events WHERE configurationId LIKE :configurationId AND ownerId LIKE :ownerId")
    fun getAllByConfigurationAndOwner(
        configurationId: String,
        ownerId: String,
    ): List<EventEntity>

    /**
     * Get timetable event entity [id]
     */
    @Query("SELECT * FROM events WHERE id LIKE :id ")
    suspend fun getById(id: String): EventEntity?

    /**
     * Insert new timetable event [entity]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: EventEntity)

    /**
     * Insert new timetable events [entities]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<EventEntity>)

    @Transaction
    suspend fun insertAndUpdateAll(
        configurationId: String,
        ownerId: String,
        entities: List<EventEntity>,
    ) {
        val cachedEvents = getAllByConfigurationAndOwner(configurationId, ownerId)
        val mappedEvents = entities.map { event ->
            val cachedEvent = cachedEvents.find { it.id == event.id }
            when {
                cachedEvent != null -> event.copy(isVisible = cachedEvent.isVisible)
                else -> event
            }
        }

        deleteAllByConfigurationAndOwner(configurationId, ownerId)
        insertAll(mappedEvents)
    }

    /**
     * Delete timetable event entity with [entityId]
     */
    @Query("DELETE FROM events WHERE id LIKE :entityId")
    suspend fun delete(entityId: String)

    /**
     * Delete all timetable event entities by [configurationId]
     */
    @Query("DELETE FROM events WHERE configurationId LIKE :configurationId")
    suspend fun deleteAllByConfiguration(configurationId: String)

    /**
     * Delete all timetable event entities by [configurationId] and [ownerId]
     */
    @Query("DELETE FROM events WHERE configurationId LIKE :configurationId AND ownerId LIKE :ownerId")
    suspend fun deleteAllByConfigurationAndOwner(configurationId: String, ownerId: String)
}
