package com.ubb.fmi.orar.feature.news.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.ui.catalog.components.skeleton.ShimmerBar
import com.ubb.fmi.orar.ui.catalog.components.skeleton.SkeletonColumn
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds

private const val ARTICLES_COUNT = 6
private const val TEXT_LINES_COUNT = 3
private const val LAST_LINE_WIDTH_FRACTION = 0.6f
private const val TITLE_LAST_LINE_WIDTH_FRACTION = 0.7f

/**
 * Placeholder of the news list displayed while the articles load, mirroring [ArticleListItem]
 * entries separated by dividers.
 */
@Composable
fun NewsSkeleton(modifier: Modifier = Modifier) {
    SkeletonColumn(modifier = modifier) { brush ->
        repeat(ARTICLES_COUNT) { index ->
            ArticleSkeleton(brush)

            if (index != ARTICLES_COUNT - 1) {
                Spacer(modifier = Modifier.padding(Pds.spacing.Medium))
                HorizontalDivider()
            }
        }
    }
}

/**
 * Placeholder of [ArticleListItem]
 */
@Composable
private fun ArticleSkeleton(brush: Brush) {
    Column(verticalArrangement = Arrangement.spacedBy(Pds.spacing.Small)) {
        Row(horizontalArrangement = Arrangement.spacedBy(Pds.spacing.Small)) {
            ShimmerBar(
                brush = brush,
                height = Pds.icon.XXLarge,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.size(Pds.icon.XXLarge)
            )

            Column(verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall)) {
                ShimmerBar(brush = brush, height = 12.dp, modifier = Modifier.width(72.dp))
                ShimmerBar(brush = brush, height = 18.dp, modifier = Modifier.fillMaxWidth())
                ShimmerBar(
                    brush = brush,
                    height = 18.dp,
                    modifier = Modifier.fillMaxWidth(TITLE_LAST_LINE_WIDTH_FRACTION)
                )
            }
        }

        repeat(TEXT_LINES_COUNT) { line ->
            ShimmerBar(
                brush = brush,
                height = 14.dp,
                modifier = Modifier.fillMaxWidth(
                    if (line == TEXT_LINES_COUNT - 1) LAST_LINE_WIDTH_FRACTION else 1f
                )
            )
        }
    }
}

@Preview
@Composable
private fun PreviewNewsSkeleton() {
    OrarUbbFmiTheme {
        NewsSkeleton()
    }
}
