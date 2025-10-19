package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.RoomEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing room entities in the database.
 */
@Dao
interface RoomDao {

    /**
     * Get all room entities as [Flow] by [configurationId]
     */
    @Query("SELECT * FROM rooms WHERE configurationId LIKE :configurationId")
    fun getAllAsFlow(configurationId: String): Flow<List<RoomEntity>>

    /**
     * Insert new room [entities]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<RoomEntity>)

    /**
     * Delete all room entities by [configurationId]
     */
    @Query("DELETE FROM rooms WHERE configurationId LIKE :configurationId")
    suspend fun deleteAll(configurationId: String)
}
