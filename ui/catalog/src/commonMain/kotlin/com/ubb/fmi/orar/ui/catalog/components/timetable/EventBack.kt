package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable for the face part of an Event back
 */
@Composable
fun EventBack(
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(Pds.spacing.Medium)) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun PreviewEventBack() {
    OrarUbbFmiTheme {
        EventBack(
            text = "Str. Teodor Mihali nr. 38-40"
        )
    }
}