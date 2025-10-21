package com.ubb.fmi.orar.feature.news.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ubb.fmi.orar.data.news.model.Article
import com.ubb.fmi.orar.data.news.model.ArticleType
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.image_placeholder
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ArticleListItem(
    article: Article,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Pds.spacing.Small),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Pds.spacing.Small)) {
            article.imageUrl?.let {
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = null,
                    placeholder = painterResource(Res.drawable.image_placeholder),
                    error = painterResource(Res.drawable.image_placeholder),
                    fallback = painterResource(Res.drawable.image_placeholder),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(Pds.icon.XXLarge)
                        .clip(MaterialTheme.shapes.small)
                        .border(
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            shape = MaterialTheme.shapes.small
                        )
                )
            }

            Column {
                Text(
                    text = article.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }

        Text(
            text = article.text,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
private fun PreviewArticleListItem() {
    OrarUbbFmiTheme {
        Surface {
            ArticleListItem(
                onClick = {},
                article = Article(
                    id = "",
                    title = "Article Title",
                    text = "This is a very large long long long long text of a news article.",
                    date = "05.11.2000",
                    url = "",
                    type = ArticleType.STUDENT,
                    imageUrl =
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSGoUqEBpSoZlRKjPIKNXSrCsAzeMEO4YXZmg&s"
                )
            )
        }
    }
}