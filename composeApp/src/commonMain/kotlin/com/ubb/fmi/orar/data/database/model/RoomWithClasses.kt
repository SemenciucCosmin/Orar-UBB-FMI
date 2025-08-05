package com.ubb.fmi.orar.data.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class RoomWithClasses(
    @Embedded
    val roomEntity: RoomEntity,

    @Relation(parentColumn = "id", entityColumn = "roomId")
    val classesEntities: List<RoomClassEntity>
)
