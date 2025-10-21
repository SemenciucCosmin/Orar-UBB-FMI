package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.ArticleEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing article entities in the database.
 */
@Dao
interface NewsDao {

    /**
     * Get all article entities as [Flow]
     */
    @Query("SELECT * FROM articles")
    fun getAllAsFlow(): Flow<List<ArticleEntity>>

    /**
     * Insert new article [entities]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<ArticleEntity>)

    /**
     * Delete all article entities
     */
    @Query("DELETE FROM articles")
    suspend fun deleteAll()
}
