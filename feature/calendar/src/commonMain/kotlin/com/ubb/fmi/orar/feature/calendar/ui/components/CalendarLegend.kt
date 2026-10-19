package com.ubb.fmi.orar.feature.calendar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.domain.colorpack.model.ColorGroup
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem
import com.ubb.fmi.orar.ui.catalog.extensions.labelRes
import com.ubb.fmi.orar.ui.colorpack.LocalColorPack
import com.ubb.fmi.orar.ui.colorpack.components.ColorDot
import com.ubb.fmi.orar.ui.colorpack.extensions.getColor
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import org.jetbrains.compose.resources.stringResource

/**
 * Displays the color of every academic activity next to its name.
 */
@Composable
fun CalendarLegend(modifier: Modifier = Modifier) {
    val colorPack = LocalColorPack.current

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(Pds.spacing.Small),
        modifier = modifier
    ) {
        ColorItem.entries.filter { it.group == ColorGroup.ACADEMIC_CALENDAR }.forEach { item ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ColorDot(
                    color = colorPack.getColor(item),
                    size = Pds.icon.XSmall,
                )

                Text(
                    text = stringResource(item.labelRes),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCalendarLegend() {
    OrarUbbFmiTheme {
        CalendarLegend()
    }
}
