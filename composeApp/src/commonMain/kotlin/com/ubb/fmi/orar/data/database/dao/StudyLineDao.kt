package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.StudyLineEntity

@Dao
interface StudyLineDao {
    @Query("SELECT * FROM study_lines WHERE configurationId LIKE :configurationId")
    suspend fun getAll(configurationId: String): List<StudyLineEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: StudyLineEntity)

    @Query("DELETE FROM study_lines WHERE configurationId LIKE :configurationId")
    suspend fun deleteAll(configurationId: String)
}
