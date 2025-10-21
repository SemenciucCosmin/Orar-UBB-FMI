package com.ubb.fmi.orar.domain.htmlparser.model

/**
 * Data class model for a single news post
 */
data class HtmlArticle(
    val title: String,
    val text: String,
    val date: String,
    val url: String,
    val imageUrl: String?,
)
