package com.ubb.fmi.orar.feature.news.ui.viewmodel.model

import com.ubb.fmi.orar.data.news.model.Article
import com.ubb.fmi.orar.data.news.model.ArticleType
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * NewsUiState represents the state of the news screen in the application.
 */
data class NewsUiState(
    private val articles: ImmutableList<Article> = persistentListOf(),
    val selectedArticleType: ArticleType = ArticleType.STUDENT,
    val isLoading: Boolean = true,
    val errorStatus: ErrorStatus? = null,
) {
    companion object {
        val NewsUiState.studentArticles: ImmutableList<Article>
            get() = articles.filter { article ->
                article.type == ArticleType.STUDENT
            }.sortedByDescending { it.millis }.toImmutableList()

        val NewsUiState.teacherArticles: ImmutableList<Article>
            get() = articles.filter { article ->
                article.type == ArticleType.TEACHER
            }.sortedByDescending { it.millis }.toImmutableList()
    }
}
