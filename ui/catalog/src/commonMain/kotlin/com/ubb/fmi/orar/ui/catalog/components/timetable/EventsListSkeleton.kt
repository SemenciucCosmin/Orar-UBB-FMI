package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.ui.catalog.components.skeleton.ShimmerBar
import com.ubb.fmi.orar.ui.catalog.components.skeleton.SkeletonColumn
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds

private val SECTIONS_EVENTS_COUNT = listOf(3, 4, 2, 3, 3)

/**
 * Placeholder of [EventsList] displayed while the timetable loads, mirroring the layout of the
 * day dividers and event cards with shimmering bars.
 */
@Composable
fun EventsListSkeleton(modifier: Modifier = Modifier) {
    SkeletonColumn(
        modifier = modifier,
        contentPadding = PaddingValues(Pds.spacing.SMedium),
    ) { brush ->
        SECTIONS_EVENTS_COUNT.forEach { eventsCount ->
            DividerSkeleton(brush)
            repeat(eventsCount) { EventCardSkeleton(brush) }
        }
    }
}

/**
 * Placeholder of [TimetableListDivider]
 */
@Composable
private fun DividerSkeleton(brush: Brush) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Pds.spacing.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShimmerBar(brush = brush, height = 16.dp, modifier = Modifier.width(72.dp))
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Placeholder of [EventCard], following the layout of [EventFace]
 */
@Suppress("MagicNumber")
@Composable
private fun EventCardSkeleton(brush: Brush) {
    ElevatedCard(
        shape = CardDefaults.elevatedShape,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(Pds.spacing.SMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.15f),
                verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ShimmerBar(brush = brush, height = 14.dp, modifier = Modifier.width(36.dp))
                ShimmerBar(brush = brush, height = 14.dp, modifier = Modifier.width(36.dp))
            }

            Column(
                modifier = Modifier.weight(0.70f),
                verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ShimmerBar(brush = brush, height = 16.dp, modifier = Modifier.width(160.dp))
                ShimmerBar(
                    brush = brush,
                    height = 24.dp,
                    shape = RoundedCornerShape(Pds.spacing.Small),
                    modifier = Modifier.width(72.dp)
                )
                ShimmerBar(brush = brush, height = 14.dp, modifier = Modifier.width(120.dp))
            }

            Box(
                modifier = Modifier.weight(0.15f),
                contentAlignment = Alignment.Center
            ) {
                ShimmerBar(brush = brush, height = 14.dp, modifier = Modifier.size(36.dp, 14.dp))
            }
        }
    }
}

@Preview
@Composable
private fun PreviewEventsListSkeleton() {
    OrarUbbFmiTheme {
        EventsListSkeleton()
    }
}
