package com.ubb.fmi.orar.feature.calendar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.ui.catalog.components.skeleton.ShimmerBar
import com.ubb.fmi.orar.ui.catalog.components.skeleton.SkeletonColumn
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds

private const val WEEKS_COUNT = 6
private const val DAYS_IN_WEEK = 7
private const val LEGEND_ITEMS_COUNT = 6
private const val PERIODS_COUNT = 5

/**
 * Placeholder of the calendar screen displayed while the academic calendar loads, mirroring the
 * month header, the month grid, the legend and the periods.
 */
@Composable
fun CalendarSkeleton(modifier: Modifier = Modifier) {
    SkeletonColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = Pds.spacing.Medium),
    ) { brush ->
        val horizontalPadding = Modifier.padding(horizontal = Pds.spacing.Medium)

        MonthHeaderSkeleton(brush = brush, modifier = horizontalPadding)
        ElevatedCard(modifier = horizontalPadding.fillMaxWidth()) {
            MonthGridSkeleton(brush = brush, modifier = Modifier.padding(Pds.spacing.SMedium))
        }
        LegendSkeleton(brush = brush, modifier = horizontalPadding)
        PeriodsSkeleton(brush = brush, modifier = horizontalPadding)
    }
}

/**
 * Placeholder of [MonthHeader]
 */
@Composable
private fun MonthHeaderSkeleton(brush: Brush, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        ShimmerBar(
            brush = brush,
            height = Pds.icon.Small,
            shape = CircleShape,
            modifier = Modifier.size(Pds.icon.Small)
        )
        ShimmerBar(brush = brush, height = 20.dp, modifier = Modifier.width(120.dp))
        ShimmerBar(
            brush = brush,
            height = Pds.icon.Small,
            shape = CircleShape,
            modifier = Modifier.size(Pds.icon.Small)
        )
    }
}

/**
 * Placeholder of [MonthGrid]
 */
@Composable
private fun MonthGridSkeleton(brush: Brush, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
        modifier = modifier
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall)) {
            Spacer(modifier = Modifier.width(Pds.icon.Small))

            repeat(DAYS_IN_WEEK) {
                ShimmerBar(brush = brush, height = 12.dp, modifier = Modifier.weight(1f))
            }
        }

        repeat(WEEKS_COUNT) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShimmerBar(brush = brush, height = 12.dp, modifier = Modifier.width(Pds.icon.Small))

                repeat(DAYS_IN_WEEK) {
                    ShimmerBar(
                        brush = brush,
                        height = Dp.Unspecified,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                    )
                }
            }
        }
    }
}

/**
 * Placeholder of [CalendarLegend]
 */
@Composable
private fun LegendSkeleton(brush: Brush, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Pds.spacing.Small),
        modifier = modifier
    ) {
        repeat(LEGEND_ITEMS_COUNT / 2) {
            Row(horizontalArrangement = Arrangement.spacedBy(Pds.spacing.Medium)) {
                repeat(2) { LegendItemSkeleton(brush) }
            }
        }
    }
}

@Composable
private fun LegendItemSkeleton(brush: Brush) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShimmerBar(
            brush = brush,
            height = Pds.icon.XSmall,
            shape = CircleShape,
            modifier = Modifier.size(Pds.icon.XSmall)
        )
        ShimmerBar(brush = brush, height = 14.dp, modifier = Modifier.width(96.dp))
    }
}

/**
 * Placeholder of [CalendarPeriods]
 */
@Composable
private fun PeriodsSkeleton(brush: Brush, modifier: Modifier = Modifier) {
    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        repeat(PERIODS_COUNT) { index ->
            if (index > 0) HorizontalDivider()

            Row(
                horizontalArrangement = Arrangement.spacedBy(Pds.spacing.Small),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Pds.spacing.Medium, vertical = Pds.spacing.SMedium)
            ) {
                ShimmerBar(
                    brush = brush,
                    height = Pds.icon.XSmall,
                    shape = CircleShape,
                    modifier = Modifier.size(Pds.icon.XSmall)
                )

                Column(verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall)) {
                    ShimmerBar(brush = brush, height = 14.dp, modifier = Modifier.width(140.dp))
                    ShimmerBar(brush = brush, height = 12.dp, modifier = Modifier.width(160.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCalendarSkeleton() {
    OrarUbbFmiTheme {
        CalendarSkeleton()
    }
}
