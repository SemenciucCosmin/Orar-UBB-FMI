package com.ubb.fmi.orar.data.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class SubjectWithClasses(
    @Embedded
    val subjectEntity: SubjectEntity,

    @Relation(parentColumn = "id", entityColumn = "subjectId")
    val classesEntities: List<SubjectClassEntity>
)
