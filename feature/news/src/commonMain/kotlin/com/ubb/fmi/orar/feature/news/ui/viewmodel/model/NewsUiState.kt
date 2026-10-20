package com.ubb.fmi.orar.feature.news.ui.viewmodel.model

import com.ubb.fmi.orar.data.news.model.Article
import com.ubb.fmi.orar.data.news.model.ArticleType
import com.ubb.fmi.orar.feature.news.ui.model.ArticlesMonth
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

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
        val NewsUiState.hasArticles: Boolean
            get() = articles
                .filter { it.type == selectedArticleType }
                .sortedByDescending { it.millis }
                .isNotEmpty()

        /**
         * Whether articles are reloaded while the previous ones are still displayed
         */
        val NewsUiState.isRefreshing: Boolean
            get() = isLoading && articles.isNotEmpty()

        /**
         * Articles of the selected type, grouped by the month they were published in, newest first
         */
        val NewsUiState.articlesByMonth: ImmutableList<ArticlesMonth>
            get() = articles
                .filter { it.type == selectedArticleType }
                .sortedByDescending { it.millis }
                .toImmutableList()
                .groupBy { article ->
                    val date = Instant.fromEpochMilliseconds(article.millis)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .date

                    date.year to date.month
                }
                .map { (yearMonth, articles) ->
                    ArticlesMonth(
                        year = yearMonth.first,
                        month = yearMonth.second,
                        articles = articles.toImmutableList()
                    )
                }
                .toImmutableList()
    }
}
