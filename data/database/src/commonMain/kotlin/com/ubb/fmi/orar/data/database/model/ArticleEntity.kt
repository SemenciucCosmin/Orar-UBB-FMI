package com.ubb.fmi.orar.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Entity for article model
 */
@Entity(
    tableName = "articles",
    primaryKeys = ["id"]
)
data class ArticleEntity(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "typeId") val typeId: String,
    @ColumnInfo(name = "imageUrl") val imageUrl: String?,
)
