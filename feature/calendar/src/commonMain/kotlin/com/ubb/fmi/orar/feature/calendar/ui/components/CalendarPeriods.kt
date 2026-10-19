package com.ubb.fmi.orar.feature.calendar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.data.calendar.model.AcademicActivity
import com.ubb.fmi.orar.data.calendar.model.AcademicPeriod
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.feature.calendar.ui.extensions.colorItem
import com.ubb.fmi.orar.feature.calendar.ui.extensions.labelRes
import com.ubb.fmi.orar.ui.colorpack.LocalColorPack
import com.ubb.fmi.orar.ui.colorpack.components.ColorDot
import com.ubb.fmi.orar.ui.colorpack.extensions.getColor
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

/**
 * Displays every period of the academic year on a card, with its activity and its start and end
 * dates, each delimited by a divider.
 */
@Composable
fun CalendarPeriods(
    periods: ImmutableList<AcademicPeriod>,
    modifier: Modifier = Modifier,
) {
    val colorPack = LocalColorPack.current

    ElevatedCard(modifier = modifier) {
        periods.forEachIndexed { index, period ->
            if (index > 0) HorizontalDivider()

            Row(
                horizontalArrangement = Arrangement.spacedBy(Pds.spacing.Small),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Pds.spacing.Medium, vertical = Pds.spacing.SMedium)
            ) {
                ColorDot(
                    color = colorPack.getColor(period.activity.colorItem),
                    size = Pds.icon.XSmall,
                )

                Column {
                    Text(
                        text = stringResource(period.activity.labelRes),
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Text(
                        text = "${formatDate(period.startDateMillis)} ${String.DASH} " +
                            formatDate(period.endDateMillis),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

/**
 * Formats the date of [millis] as dd.MM.yyyy
 */
private fun formatDate(millis: Long): String {
    return Instant.fromEpochMilliseconds(millis)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
        .format(
            LocalDate.Format {
                day()
                char('.')
                monthNumber()
                char('.')
                year()
            }
        )
}

@Preview
@Composable
private fun PreviewCalendarPeriods() {
    OrarUbbFmiTheme {
        CalendarPeriods(
            periods = persistentListOf(
                AcademicPeriod(
                    activity = AcademicActivity.TEACHING_ACTIVITY,
                    weeks = emptyList(),
                    startDateMillis = 1759093200000,
                    endDateMillis = 1766268000000,
                ),
                AcademicPeriod(
                    activity = AcademicActivity.CHRISTMAS_HOLIDAY,
                    weeks = emptyList(),
                    startDateMillis = 1766354400000,
                    endDateMillis = 1767477600000,
                ),
            )
        )
    }
}
