package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ubb.fmi.orar.data.database.di.Car
import com.ubb.fmi.orar.data.database.model.RoomClassEntity
import com.ubb.fmi.orar.data.database.model.RoomEntity
import com.ubb.fmi.orar.data.database.model.RoomWithClasses
import com.ubb.fmi.orar.data.database.model.TeacherClassEntity
import com.ubb.fmi.orar.data.database.model.TeacherEntity
import com.ubb.fmi.orar.data.database.model.TeacherWithClasses
import kotlinx.coroutines.flow.Flow

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
