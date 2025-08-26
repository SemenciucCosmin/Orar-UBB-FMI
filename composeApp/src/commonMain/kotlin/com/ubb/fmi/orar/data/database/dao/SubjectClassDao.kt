package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.SubjectClassEntity

@Dao
interface SubjectClassDao {

    @Query("SELECT * FROM subject_classes")
    suspend fun getAllSubjectClasses(): List<SubjectClassEntity>

    @Query("SELECT * FROM subject_classes WHERE subjectId LIKE :subjectId")
    suspend fun getSubjectClasses(subjectId: String): List<SubjectClassEntity>

    @Query("SELECT * FROM subject_classes WHERE id LIKE :subjectClassId")
    suspend fun getSubjectClass(subjectClassId: String): SubjectClassEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjectClass(subjectClass: SubjectClassEntity)
}
