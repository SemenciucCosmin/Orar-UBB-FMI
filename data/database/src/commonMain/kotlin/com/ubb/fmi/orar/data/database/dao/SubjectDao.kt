package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.SubjectEntity

/**
 * Data Access Object (DAO) for managing subject entities in the database.
 * This interface provides methods to interact with the 'subjects' table,
 * including retrieving, inserting, and deleting subject records.
 */
@Dao
interface SubjectDao {

    /**
     * Get all subject entities by [configurationId]
     */
    @Query("SELECT * FROM subjects WHERE configurationId LIKE :configurationId")
    suspend fun getAll(configurationId: String): List<SubjectEntity>

    /**
     * Insert new subject [entity]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SubjectEntity)

    /**
     * Delete all subject entities by [configurationId]
     */
    @Query("DELETE FROM subjects WHERE configurationId LIKE :configurationId")
    suspend fun deleteAll(configurationId: String)
}
