package com.ubb.fmi.orar.data.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class TeacherWithClasses(
    @Embedded
    val teacherEntity: TeacherEntity,

    @Relation(parentColumn = "id", entityColumn = "teacherId")
    val classesEntities: List<TeacherClassEntity>
)
