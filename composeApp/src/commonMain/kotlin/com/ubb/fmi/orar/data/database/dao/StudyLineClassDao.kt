package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ubb.fmi.orar.data.database.model.StudyLineEntity
import com.ubb.fmi.orar.data.database.model.StudyLineClassEntity

@Dao
interface StudyLineClassDao {
    @Query("SELECT * FROM study_line_classes")
    suspend fun getAllStudyLineClasses(): List<StudyLineClassEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyLineClass(studyLineClassEntity: StudyLineClassEntity)
}
