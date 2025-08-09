package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ubb.fmi.orar.data.database.model.StudyLineEntity
import com.ubb.fmi.orar.data.database.model.StudyLineClassEntity
import com.ubb.fmi.orar.data.database.model.StudyLineWithClasses

@Dao
interface StudyLineDao {
    @Query("SELECT * FROM study_lines")
    suspend fun getAllStudyLines(): List<StudyLineEntity>

    @Transaction
    @Query("SELECT * FROM study_lines")
    suspend fun getAllStudyLinesWithClasses(): List<StudyLineWithClasses>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyLine(studyLineEntity: StudyLineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyLineClasses(studyLineClasses: List<StudyLineClassEntity>)
}
