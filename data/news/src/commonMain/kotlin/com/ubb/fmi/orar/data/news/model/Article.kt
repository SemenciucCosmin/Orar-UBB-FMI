package com.ubb.fmi.orar.data.news.model

/**
 * Data class model for a single news post
 */
data class Article(
    val id: String,
    val title: String,
    val text: String,
    val millis: Long,
    val url: String,
    val type: ArticleType,
    val imageUrl: String?,
)
