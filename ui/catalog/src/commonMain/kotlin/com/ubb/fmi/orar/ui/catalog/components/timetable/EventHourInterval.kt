@file:Suppress("MagicNumber")

package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_down_arrow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable that displays a the event hour interval in a column
 */
@Composable
fun EventHourInterval(
    startHour: String,
    endHour: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = startHour,
            style = MaterialTheme.typography.bodyMedium
        )

        Icon(
            modifier = Modifier.size(Pds.icon.SMedium),
            painter = painterResource(Res.drawable.ic_down_arrow),
            contentDescription = null
        )

        Text(
            text = endHour,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
private fun PreviewEventHourInterval() {
    OrarUbbFmiTheme {
        EventHourInterval(
            startHour = "14:00",
            endHour = "16:00",
        )
    }
}
