package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ubb.fmi.orar.data.database.model.TeacherClassEntity
import com.ubb.fmi.orar.data.database.model.TeacherEntity
import com.ubb.fmi.orar.data.database.model.TeacherWithClasses
import com.ubb.fmi.orar.data.teachers.model.TeacherClass

@Dao
interface TeacherClassDao {

    @Query("SELECT * FROM teacher_classes")
    suspend fun getAllTeacherClasses(): List<TeacherClassEntity>

    @Query("SELECT * FROM teacher_classes WHERE teacherId LIKE :teacherId")
    suspend fun getTeacherClasses(teacherId: String): List<TeacherClassEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacherClass(teacherClass: TeacherClassEntity)
}
