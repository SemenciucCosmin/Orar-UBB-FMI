package com.ubb.fmi.orar.feature.calendar.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.ubb.fmi.orar.ui.catalog.extensions.labelRes
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.datetime.LocalDate
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_left_arrow
import orar_ubb_fmi.ui.catalog.generated.resources.ic_right_arrow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Displays the month and year of [monthDate] between buttons for the previous and next months.
 */
@Composable
fun MonthHeader(
    monthDate: LocalDate,
    isPreviousEnabled: Boolean,
    isNextEnabled: Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            FilledTonalIconButton(
                enabled = isPreviousEnabled,
                onClick = onPreviousClick,
                modifier = Modifier.size(Pds.icon.Small)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_left_arrow),
                    contentDescription = null,
                    modifier = Modifier.size(Pds.icon.XSmall)
                )
            }

            Text(
                text = "${stringResource(monthDate.month.labelRes)} ${monthDate.year}",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            FilledTonalIconButton(
                enabled = isNextEnabled,
                onClick = onNextClick,
                modifier = Modifier.size(Pds.icon.Small)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_right_arrow),
                    contentDescription = null,
                    modifier = Modifier.size(Pds.icon.XSmall)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewMonthHeader() {
    OrarUbbFmiTheme {
        Surface {
            MonthHeader(
                monthDate = LocalDate(2025, 10, 1),
                isPreviousEnabled = false,
                isNextEnabled = true,
                onPreviousClick = {},
                onNextClick = {},
            )
        }
    }
}
