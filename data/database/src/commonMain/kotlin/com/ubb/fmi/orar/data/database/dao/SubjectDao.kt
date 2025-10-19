package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.SubjectEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing subject entities in the database.
 */
@Dao
interface SubjectDao {

    /**
     * Get all subject entities as [Flow] by [configurationId]
     */
    @Query("SELECT * FROM subjects WHERE configurationId LIKE :configurationId")
    fun getAllAsFlow(configurationId: String): Flow<List<SubjectEntity>>

    /**
     * Insert new subject [entities]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<SubjectEntity>)

    /**
     * Delete all subject entities by [configurationId]
     */
    @Query("DELETE FROM subjects WHERE configurationId LIKE :configurationId")
    suspend fun deleteAll(configurationId: String)
}
