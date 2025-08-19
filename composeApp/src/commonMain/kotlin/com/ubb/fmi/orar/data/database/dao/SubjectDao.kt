package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.SubjectEntity

@Dao
interface SubjectDao {

    @Query("SELECT * FROM subjects")
    suspend fun getAllSubjects(): List<SubjectEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: SubjectEntity)
}
