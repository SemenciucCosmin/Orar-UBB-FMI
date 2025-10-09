package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.GroupEntity

/**
 * Data Access Object (DAO) for managing group entities in the database.
 * This interface provides methods to interact with the 'groups' table,
 * including retrieving, inserting, and deleting study line records.
 */
@Dao
interface GroupDao {

    /**
     * Get all group entities by [configurationId]
     */
    @Query("SELECT * FROM `groups` WHERE studyLineId LIKE :studyLineId AND configurationId LIKE :configurationId")
    suspend fun getAll(configurationId: String, studyLineId: String): List<GroupEntity>

    /**
     * Insert new group [entity]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: GroupEntity)

    /**
     * Delete all groups entities by [configurationId]
     */
    @Query("DELETE FROM `groups` WHERE configurationId LIKE :configurationId")
    suspend fun deleteAll(configurationId: String)
}
