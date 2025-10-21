package com.ubb.fmi.orar.feature.news.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.data.news.model.Article
import com.ubb.fmi.orar.data.news.model.ArticleType
import com.ubb.fmi.orar.feature.news.ui.viewmodel.model.NewsUiState
import com.ubb.fmi.orar.feature.news.ui.viewmodel.model.NewsUiState.Companion.studentArticles
import com.ubb.fmi.orar.feature.news.ui.viewmodel.model.NewsUiState.Companion.teacherArticles
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.state.StateScaffold
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.toImmutableList
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_news
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * NewsScreen is a composable function that displays a list of news with filtering options.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    uiState: NewsUiState,
    onArticleClick: (String) -> Unit,
    onRetryClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    var selectedArticleType by remember { mutableStateOf(uiState.selectedArticleType) }
    val articles by remember(uiState) {
        derivedStateOf {
            when (selectedArticleType) {
                ArticleType.STUDENT -> uiState.studentArticles
                ArticleType.TEACHER -> uiState.teacherArticles
            }
        }
    }

    StateScaffold(
        isLoading = uiState.isLoading,
        isEmpty = articles.isEmpty(),
        errorStatus = uiState.errorStatus,
        onRetryClick = onRetryClick,
        bottomBar = bottomBar,
        topBar = {
            TopBar(
                title = stringResource(Res.string.lbl_news),
                titleStyle = MaterialTheme.typography.headlineMedium,
                trailingContent = {
                    NewsArticleTypeTab(
                        selectedArticleType = selectedArticleType,
                        onArticleTypeClick = { selectedArticleType = it },
                    )
                }
            )
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(
                contentPadding = PaddingValues(Pds.spacing.Medium),
                verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
            ) {
                itemsIndexed(articles) { index, article ->
                    ArticleListItem(
                        article = article,
                        onClick = { onArticleClick(article.url) }
                    )

                    if (index != articles.lastIndex) {
                        Spacer(modifier = Modifier.padding(Pds.spacing.Medium))
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewNewsScreen() {
    OrarUbbFmiTheme {
        NewsScreen(
            onArticleClick = {},
            onRetryClick = {},
            bottomBar = {},
            uiState = NewsUiState(
                articles = List(10) {
                    Article(
                        id = it.toString(),
                        title = "Title $it",
                        text = "Article very long long long long text $it",
                        date = "08.10.2025",
                        url = "",
                        type = ArticleType.STUDENT,
                        imageUrl =
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSGoUqEBpSoZlRKjPIKNXSrCsAzeMEO4YXZmg&s"
                    )
                }.toImmutableList(),
            )
        )
    }
}
