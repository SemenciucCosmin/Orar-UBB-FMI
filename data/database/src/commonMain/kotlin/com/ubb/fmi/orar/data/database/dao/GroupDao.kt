package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.GroupEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing group entities in the database.
 */
@Dao
interface GroupDao {

    /**
     * Retrieves all group entities from database as [Flow] by [configurationId] and [studyLineId]
     */
    @Query("SELECT * FROM `groups` WHERE studyLineId LIKE :studyLineId AND configurationId LIKE :configurationId")
    fun getAllAsFlow(configurationId: String, studyLineId: String): Flow<List<GroupEntity>>

    /**
     * Inserts new group [entities]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<GroupEntity>)

    /**
     * Delete all groups entities by [configurationId]
     */
    @Query("DELETE FROM `groups` WHERE configurationId LIKE :configurationId")
    suspend fun deleteAll(configurationId: String)
}
