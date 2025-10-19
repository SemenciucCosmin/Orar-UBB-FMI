package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.TeacherEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing teacher entities in the database.
 */
@Dao
interface TeacherDao {

    /**
     * Get all teacher entities as [Flow] by [configurationId]
     */
    @Query("SELECT * FROM teachers WHERE configurationId LIKE :configurationId")
    fun getAllAsFlow(configurationId: String): Flow<List<TeacherEntity>>

    /**
     * Insert new teacher [entities]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<TeacherEntity>)

    /**
     * Delete all teacher entities by [configurationId]
     */
    @Query("DELETE FROM teachers WHERE configurationId LIKE :configurationId")
    suspend fun deleteAll(configurationId: String)
}
