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

    @Query("SELECT * FROM teachers WHERE id LIKE :teacherId")
    suspend fun getTeacher(teacherId: String): TeacherEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teacher: TeacherEntity)
}
