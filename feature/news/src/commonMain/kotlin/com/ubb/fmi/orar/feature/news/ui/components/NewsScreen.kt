package com.ubb.fmi.orar.feature.news.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.data.news.model.Article
import com.ubb.fmi.orar.data.news.model.ArticleType
import com.ubb.fmi.orar.feature.news.ui.viewmodel.model.NewsUiState
import com.ubb.fmi.orar.feature.news.ui.viewmodel.model.NewsUiState.Companion.articlesByMonth
import com.ubb.fmi.orar.feature.news.ui.viewmodel.model.NewsUiState.Companion.hasArticles
import com.ubb.fmi.orar.feature.news.ui.viewmodel.model.NewsUiState.Companion.isRefreshing
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.state.StateScaffold
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import kotlinx.collections.immutable.toImmutableList
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_news
import org.jetbrains.compose.resources.stringResource

/**
 * NewsScreen is a composable function that displays the news of the selected audience.
 */
@Composable
fun NewsScreen(
    uiState: NewsUiState,
    onArticleClick: (Article) -> Unit,
    onArticleTypeClick: (ArticleType) -> Unit,
    onRetryClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    StateScaffold(
        isLoading = uiState.isLoading && !uiState.isRefreshing,
        isEmpty = !uiState.hasArticles,
        errorStatus = uiState.errorStatus,
        onRetryClick = onRetryClick,
        loadingContent = { paddingValues ->
            NewsSkeleton(modifier = Modifier.padding(paddingValues))
        },
        bottomBar = bottomBar,
        topBar = {
            Column {
                TopBar(
                    title = stringResource(Res.string.lbl_news),
                    titleStyle = MaterialTheme.typography.headlineMedium,
                )

                NewsTabRow(
                    selectedArticleType = uiState.selectedArticleType,
                    onArticleTypeClick = onArticleTypeClick,
                )
            }
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRetryClick,
            modifier = Modifier.padding(paddingValues)
        ) {
            NewsList(
                articlesByMonth = uiState.articlesByMonth,
                onArticleClick = onArticleClick,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewNewsScreen() {
    OrarUbbFmiTheme {
        NewsScreen(
            onArticleClick = {},
            onArticleTypeClick = {},
            onRetryClick = {},
            bottomBar = {},
            uiState = NewsUiState(
                isLoading = false,
                articles = List(10) {
                    Article(
                        id = it.toString(),
                        title = "Title $it",
                        text = "Article very long long long long text $it",
                        millis = 1761051666154 - it * 1_000_000_000L,
                        url = "",
                        type = ArticleType.STUDENT,
                        imageUrl = "",
                    )
                }.toImmutableList(),
            )
        )
    }
}
