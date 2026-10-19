package com.ubb.fmi.orar.ui.catalog.components.skeleton

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds

private const val DEFAULT_ITEMS_COUNT = 14
private const val HEADLINE_WIDTH_FRACTIONS_COUNT = 4

/**
 * Placeholder of a list of [com.ubb.fmi.orar.ui.catalog.components.list.ListItemClickable],
 * [com.ubb.fmi.orar.ui.catalog.components.list.ListItemSelectable] or
 * [com.ubb.fmi.orar.ui.catalog.components.list.ListItemExpandable] items.
 *
 * @param itemsCount Number of placeholder items, the ones not fitting on screen are clipped.
 * @param chipsCount Number of chips in a placeholder filter row above the list, none when 0.
 * @param hasLeadingIcon Whether the items have a leading icon.
 * @param hasOverline Whether the items have a line of text above the headline.
 * @param hasUnderline Whether the items have a line of text below the headline.
 * @param hasTrailingIcon Whether the items have a trailing icon.
 */
@Composable
fun ListSkeleton(
    modifier: Modifier = Modifier,
    itemsCount: Int = DEFAULT_ITEMS_COUNT,
    chipsCount: Int = 0,
    hasLeadingIcon: Boolean = false,
    hasOverline: Boolean = false,
    hasUnderline: Boolean = false,
    hasTrailingIcon: Boolean = false,
) {
    SkeletonColumn(modifier = modifier) { brush ->
        if (chipsCount > 0) ChipRowSkeleton(brush = brush, chipsCount = chipsCount)

        repeat(itemsCount) { index ->
            ListItemSkeleton(
                brush = brush,
                headlineWidthFraction = headlineWidthFraction(index),
                hasLeadingIcon = hasLeadingIcon,
                hasOverline = hasOverline,
                hasUnderline = hasUnderline,
                hasTrailingIcon = hasTrailingIcon,
            )
        }
    }
}

/**
 * Placeholder of a [com.ubb.fmi.orar.ui.catalog.components.list.ChipSelectionRow]
 */
@Composable
private fun ChipRowSkeleton(
    brush: Brush,
    chipsCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Pds.spacing.Small),
        modifier = modifier
    ) {
        repeat(chipsCount) {
            ShimmerBar(
                brush = brush,
                height = 32.dp,
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.width(72.dp)
            )
        }
    }
}

/**
 * Placeholder of a [com.ubb.fmi.orar.ui.catalog.components.list.ListRow] inside a card
 *
 * @param headlineWidthFraction Fraction of the available width taken by the headline, varying it
 * between items makes the list look more natural.
 */
@Composable
private fun ListItemSkeleton(
    brush: Brush,
    modifier: Modifier = Modifier,
    headlineWidthFraction: Float = 1f,
    hasLeadingIcon: Boolean = false,
    hasOverline: Boolean = false,
    hasUnderline: Boolean = false,
    hasTrailingIcon: Boolean = false,
) {
    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Pds.spacing.SMedium),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(Pds.spacing.SMedium)
        ) {
            if (hasLeadingIcon) {
                ShimmerBar(
                    brush = brush,
                    height = Pds.icon.SMedium,
                    modifier = Modifier.size(Pds.icon.SMedium)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
                modifier = Modifier.weight(1f)
            ) {
                if (hasOverline) {
                    ShimmerBar(brush = brush, height = 12.dp, modifier = Modifier.width(64.dp))
                }

                ShimmerBar(
                    brush = brush,
                    height = 16.dp,
                    modifier = Modifier.fillMaxWidth(headlineWidthFraction)
                )

                if (hasUnderline) {
                    ShimmerBar(brush = brush, height = 12.dp, modifier = Modifier.width(120.dp))
                }
            }

            if (hasTrailingIcon) {
                ShimmerBar(
                    brush = brush,
                    height = Pds.icon.SMedium,
                    modifier = Modifier.size(Pds.icon.SMedium)
                )
            }
        }
    }
}

@Suppress("MagicNumber")
private fun headlineWidthFraction(index: Int): Float {
    return when (index % HEADLINE_WIDTH_FRACTIONS_COUNT) {
        0 -> 0.7f
        1 -> 0.5f
        2 -> 0.85f
        else -> 0.6f
    }
}

@Preview
@Composable
private fun PreviewListSkeleton() {
    OrarUbbFmiTheme {
        ListSkeleton(
            chipsCount = 3,
            hasLeadingIcon = true,
            hasOverline = true,
            hasTrailingIcon = true,
        )
    }
}
