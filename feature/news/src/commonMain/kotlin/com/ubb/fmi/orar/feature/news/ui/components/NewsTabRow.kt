package com.ubb.fmi.orar.feature.news.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.data.news.model.ArticleType
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import org.jetbrains.compose.resources.stringResource

/**
 * Tabs for selecting the audience of the displayed articles
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsTabRow(
    selectedArticleType: ArticleType,
    onArticleTypeClick: (ArticleType) -> Unit,
    modifier: Modifier = Modifier,
) {
    PrimaryTabRow(
        selectedTabIndex = selectedArticleType.ordinal,
        modifier = modifier
    ) {
        ArticleType.entries.forEach { articleType ->
            Tab(
                selected = articleType == selectedArticleType,
                onClick = { onArticleTypeClick(articleType) },
                text = { Text(text = stringResource(articleType.labelRes)) }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewNewsTabRow() {
    OrarUbbFmiTheme {
        NewsTabRow(
            selectedArticleType = ArticleType.STUDENT,
            onArticleTypeClick = {}
        )
    }
}
