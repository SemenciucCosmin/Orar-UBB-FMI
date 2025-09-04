package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.StudyLineEntity

/**
 * Data Access Object (DAO) for managing study line entities in the database.
 * This interface provides methods to interact with the 'study_lines' table,
 * including retrieving, inserting, and deleting study line records.
 */
@Dao
interface StudyLineDao {

    /**
     * Get all study line entities by [configurationId]
     */
    @Query("SELECT * FROM study_lines WHERE configurationId LIKE :configurationId")
    suspend fun getAll(configurationId: String): List<StudyLineEntity>

    /**
     * Insert new study line [entity]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: StudyLineEntity)

    /**
     * Delete all study line entities by [configurationId]
     */
    @Query("DELETE FROM study_lines WHERE configurationId LIKE :configurationId")
    suspend fun deleteAll(configurationId: String)
}
