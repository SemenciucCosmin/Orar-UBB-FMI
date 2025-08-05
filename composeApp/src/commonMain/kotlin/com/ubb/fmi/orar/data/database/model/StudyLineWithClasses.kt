package com.ubb.fmi.orar.data.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class StudyLineWithClasses(
    @Embedded
    val studyLineEntity: StudyLineEntity,

    @Relation(parentColumn = "id", entityColumn = "studyLineId")
    val classes: List<StudyLineClassEntity>
)
