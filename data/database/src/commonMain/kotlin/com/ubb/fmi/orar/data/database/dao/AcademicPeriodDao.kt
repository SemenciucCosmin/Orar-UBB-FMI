package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.AcademicPeriodEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing academic calendar entities in the database.
 */
@Dao
interface AcademicPeriodDao {

    /**
     * Get all academic period entities as [Flow] by [startYear]
     */
    @Query("SELECT * FROM academic_periods WHERE startYear = :startYear ORDER BY startDateMillis")
    fun getAllAsFlow(startYear: Int): Flow<List<AcademicPeriodEntity>>

    /**
     * Insert new academic period [entities]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<AcademicPeriodEntity>)

    /**
     * Delete all academic period entities by [startYear]
     */
    @Query("DELETE FROM academic_periods WHERE startYear = :startYear")
    suspend fun deleteAll(startYear: Int)
}
