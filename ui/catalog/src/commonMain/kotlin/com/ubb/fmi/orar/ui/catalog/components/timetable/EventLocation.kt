@file:Suppress("MagicNumber")

package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.ui.catalog.extensions.imageRes
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable that displays a an icon and text in a
 * column for an event location
 */
@Composable
fun EventLocation(
    text: String,
    type: EventType,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(Pds.icon.Medium),
            painter = painterResource(type.imageRes),
            contentDescription = null
        )

        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
private fun PreviewEventLocation() {
    OrarUbbFmiTheme {
        EventLocation(
            text = "L402",
            type = EventType.LABORATORY
        )
    }
}