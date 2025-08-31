package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.TeacherEntity

@Dao
interface TeacherDao {

    @Query("SELECT * FROM teachers WHERE configurationId LIKE :configurationId")
    suspend fun getAll(configurationId: String): List<TeacherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TeacherEntity)

    @Query("DELETE FROM teachers WHERE configurationId LIKE :configurationId")
    suspend fun deleteAll(configurationId: String)
}
