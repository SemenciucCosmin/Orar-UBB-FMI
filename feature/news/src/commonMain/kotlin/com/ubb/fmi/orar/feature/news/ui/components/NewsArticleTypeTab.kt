package com.ubb.fmi.orar.feature.news.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.data.news.model.ArticleType
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable that displays a tab for selecting the article type of news
 */
@Composable
fun NewsArticleTypeTab(
    selectedArticleType: ArticleType,
    onArticleTypeClick: (ArticleType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChip(
            selected = selectedArticleType == ArticleType.STUDENT,
            onClick = { onArticleTypeClick(ArticleType.STUDENT) },
            shape = MaterialTheme.shapes.medium,
            label = {
                Text(
                    text = stringResource(ArticleType.STUDENT.labelRes),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        )

        FilterChip(
            selected = selectedArticleType == ArticleType.TEACHER,
            onClick = { onArticleTypeClick(ArticleType.TEACHER) },
            shape = MaterialTheme.shapes.medium,
            label = {
                Text(
                    text = stringResource(ArticleType.TEACHER.labelRes),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        )
    }
}

@Preview
@Composable
private fun PreviewNewsArticleTypeTab() {
    OrarUbbFmiTheme {
        NewsArticleTypeTab(
            selectedArticleType = ArticleType.STUDENT,
            onArticleTypeClick = {}
        )
    }
}
