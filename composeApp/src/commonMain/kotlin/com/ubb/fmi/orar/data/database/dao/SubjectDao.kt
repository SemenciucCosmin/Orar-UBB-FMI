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
import com.ubb.fmi.orar.data.database.model.SubjectClassEntity
import com.ubb.fmi.orar.data.database.model.SubjectEntity
import com.ubb.fmi.orar.data.database.model.SubjectWithClasses
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {
    @Transaction
    @Query("SELECT * FROM subjects")
    suspend fun getAllSubjectsWithClasses(): List<SubjectWithClasses>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: SubjectEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjectClasses(subjectClasses: List<SubjectClassEntity>)
}
