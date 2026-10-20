package com.ubb.fmi.orar.feature.news.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.data.news.model.Article
import com.ubb.fmi.orar.feature.news.ui.model.ArticlesMonth
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.ImmutableList

/**
 * Articles grouped by the month they were published in
 */
@Composable
fun NewsList(
    articlesByMonth: ImmutableList<ArticlesMonth>,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(Pds.spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(Pds.spacing.SMedium),
        modifier = modifier
    ) {
        articlesByMonth.forEach { articlesMonth ->
            item(key = "${articlesMonth.year}-${articlesMonth.month}") {
                NewsMonthHeader(
                    year = articlesMonth.year,
                    month = articlesMonth.month,
                    modifier = Modifier.animateItem()
                )
            }

            items(items = articlesMonth.articles, key = { it.id }) { article ->
                ArticleCard(
                    article = article,
                    onClick = { onArticleClick(article) },
                    modifier = Modifier.animateItem()
                )
            }
        }
    }
}
