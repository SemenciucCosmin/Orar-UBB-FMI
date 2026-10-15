package com.ubb.fmi.orar.feature.calendar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Week
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.feature.calendar.ui.extensions.labelRes
import com.ubb.fmi.orar.feature.calendar.ui.model.CalendarDay
import com.ubb.fmi.orar.feature.calendar.ui.model.CalendarMonth
import com.ubb.fmi.orar.feature.calendar.ui.model.CalendarWeek
import com.ubb.fmi.orar.ui.catalog.components.calendar.CalendarDayBox
import com.ubb.fmi.orar.ui.catalog.extensions.shortLabelRes
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.jetbrains.compose.resources.stringResource

private const val MAX_WEEKS_IN_MONTH = 6
private const val DAYS_IN_WEEK = 7

/**
 * Displays the days of [month] in a grid, one row per week, each row starting with the parity of
 * the week when it holds teaching days. Months with fewer weeks are completed with invisible rows,
 * so every month has the same height.
 */
@Composable
fun MonthGrid(
    month: CalendarMonth,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
        modifier = modifier
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall)) {
            Spacer(modifier = Modifier.width(Pds.icon.Small))

            Day.entries.forEach { day ->
                Text(
                    text = stringResource(day.shortLabelRes),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        month.weeks.forEach { week ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isTeachingWeek = week.days.any { it.colorItem == ColorItem.CALENDAR_TEACHING }

                Text(
                    modifier = Modifier.width(Pds.icon.Small),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    text = when {
                        isTeachingWeek -> stringResource(week.week.labelRes)
                        else -> String.BLANK
                    }
                )

                week.days.forEach { day ->
                    CalendarDayBox(
                        dayOfMonth = day.date.day,
                        colorItem = day.colorItem,
                        isToday = day.isToday,
                        isCurrentMonth = day.date.month == month.firstDate.month,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        repeat(MAX_WEEKS_IN_MONTH - month.weeks.size) {
            Row(horizontalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall)) {
                Spacer(modifier = Modifier.width(Pds.icon.Small))

                repeat(DAYS_IN_WEEK) {
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewMonthGrid() {
    val monday = LocalDate(2025, 9, 29)

    OrarUbbFmiTheme {
        MonthGrid(
            month = CalendarMonth(
                firstDate = LocalDate(2025, 10, 1),
                weeks = List(5) { weekIndex ->
                    CalendarWeek(
                        week = Week.ODD,
                        days = List(7) { dayIndex ->
                            CalendarDay(
                                date = monday.plus(weekIndex * 7 + dayIndex, DateTimeUnit.DAY),
                                colorItem = ColorItem.CALENDAR_TEACHING,
                                isToday = false,
                            )
                        }.toImmutableList()
                    )
                }.toImmutableList()
            )
        )
    }
}
