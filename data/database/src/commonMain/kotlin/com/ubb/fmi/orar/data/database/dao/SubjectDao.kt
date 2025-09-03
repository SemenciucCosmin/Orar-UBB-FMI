package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.SubjectEntity

@Dao
interface SubjectDao {

    @Query("SELECT * FROM subjects WHERE configurationId LIKE :configurationId")
    suspend fun getAll(configurationId: String): List<SubjectEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SubjectEntity)

    @Query("DELETE FROM subjects WHERE configurationId LIKE :configurationId")
    suspend fun deleteAll(configurationId: String)
}
