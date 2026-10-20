package com.ubb.fmi.orar.feature.news.ui.model

import com.ubb.fmi.orar.data.news.model.Article
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.Month

/**
 * Articles published in the same [month] of the [year], newest first
 */
data class ArticlesMonth(
    val year: Int,
    val month: Month,
    val articles: ImmutableList<Article>,
)
