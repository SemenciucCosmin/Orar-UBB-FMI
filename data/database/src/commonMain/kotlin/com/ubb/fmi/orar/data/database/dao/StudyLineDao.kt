package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.StudyLineEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing study line entities in the database.
 */
@Dao
interface StudyLineDao {

    /**
     * Get all study line entities as [Flow] by [configurationId]
     */
    @Query("SELECT * FROM study_lines WHERE configurationId LIKE :configurationId")
    fun getAllAsFlow(configurationId: String): Flow<List<StudyLineEntity>>

    /**
     * Insert new study line [entities]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<StudyLineEntity>)

    /**
     * Delete all study line entities by [configurationId]
     */
    @Query("DELETE FROM study_lines WHERE configurationId LIKE :configurationId")
    suspend fun deleteAll(configurationId: String)
}
