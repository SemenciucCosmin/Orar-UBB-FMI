package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.TeacherEntity

@Dao
interface TeacherDao {

    @Query("SELECT * FROM teachers")
    suspend fun getAllTeachers(): List<TeacherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teacher: TeacherEntity)
}
