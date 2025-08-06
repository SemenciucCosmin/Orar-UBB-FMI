package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ubb.fmi.orar.data.database.model.TeacherClassEntity
import com.ubb.fmi.orar.data.database.model.TeacherEntity
import com.ubb.fmi.orar.data.database.model.TeacherWithClasses

@Dao
interface TeacherDao {
    @Transaction
    @Query("SELECT * FROM teachers")
    suspend fun getAllTeachersWithClasses(): List<TeacherWithClasses>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teacher: TeacherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacherClasses(teacherClasses: List<TeacherClassEntity>)
}
