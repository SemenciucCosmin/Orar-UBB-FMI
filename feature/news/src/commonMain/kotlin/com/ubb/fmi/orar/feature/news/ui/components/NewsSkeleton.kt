package com.ubb.fmi.orar.feature.news.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
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

private const val ARTICLES_COUNT = 10
private const val TEXT_LAST_LINE_WIDTH_FRACTION = 0.6f

/**
 * Placeholder of the news list displayed while the articles load, mirroring a
 * [NewsMonthHeader] followed by [ArticleCard] entries.
 */
@Composable
fun NewsSkeleton(modifier: Modifier = Modifier) {
    SkeletonColumn(
        modifier = modifier,
        spacing = Pds.spacing.SMedium
    ) { brush ->
        ShimmerBar(brush = brush, height = 14.dp, modifier = Modifier.width(120.dp))
        repeat(ARTICLES_COUNT) { ArticleSkeleton(brush) }
    }
}

/**
 * Placeholder of [ArticleCard]
 */
@Composable
private fun ArticleSkeleton(brush: Brush) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Pds.spacing.SMedium),
            modifier = Modifier.padding(Pds.spacing.SMedium)
        ) {
            ShimmerBar(
                brush = brush,
                height = Pds.icon.XXLarge,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.size(Pds.icon.XXLarge)
            )

            Column(verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall)) {
                ShimmerBar(brush = brush, height = 12.dp, modifier = Modifier.width(72.dp))
                ShimmerBar(brush = brush, height = 16.dp, modifier = Modifier.fillMaxWidth())
                ShimmerBar(
                    brush = brush,
                    height = 12.dp,
                    modifier = Modifier.fillMaxWidth(TEXT_LAST_LINE_WIDTH_FRACTION)
                )
            }
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
