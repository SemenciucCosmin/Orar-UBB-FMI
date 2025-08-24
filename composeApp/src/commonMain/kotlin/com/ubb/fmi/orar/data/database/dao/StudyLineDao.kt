package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ubb.fmi.orar.data.database.model.StudyLineEntity

@Dao
interface StudyLineDao {
    @Query("SELECT * FROM study_lines")
    suspend fun getAllStudyLines(): List<StudyLineEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyLine(studyLineEntity: StudyLineEntity)
}
