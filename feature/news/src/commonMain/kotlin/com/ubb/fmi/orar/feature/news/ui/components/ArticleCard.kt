package com.ubb.fmi.orar.feature.news.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.data.news.model.Article
import com.ubb.fmi.orar.data.news.model.ArticleType
import com.ubb.fmi.orar.domain.extensions.formatToDate
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds

private const val TITLE_MAX_LINES = 2
private const val TEXT_MAX_LINES = 2

/**
 * Compact card of an article, with a thumbnail, its date, title and the beginning of its text
 */
@Composable
fun ArticleCard(
    article: Article,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Pds.spacing.SMedium),
            modifier = Modifier.padding(Pds.spacing.SMedium)
        ) {
            article.imageUrl?.let { imageUrl ->
                ArticleImage(
                    imageUrl = imageUrl,
                    modifier = Modifier.size(Pds.icon.XXLarge)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(Pds.spacing.XXSmall)) {
                Text(
                    text = article.millis.formatToDate(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )

                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = TITLE_MAX_LINES,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = article.text,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = TEXT_MAX_LINES,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewArticleCard() {
    OrarUbbFmiTheme {
        ArticleCard(
            onClick = {},
            article = Article(
                id = "",
                title = "Article Title",
                text = "This is a very large long long long long text of a news article.",
                millis = 1761051666154,
                url = "",
                type = ArticleType.STUDENT,
                imageUrl = "",
            )
        )
    }
}
