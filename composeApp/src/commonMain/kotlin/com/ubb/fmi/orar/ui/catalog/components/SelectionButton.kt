package com.ubb.fmi.orar.ui.catalog.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SelectionButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        shape = MaterialTheme.shapes.small,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = when {
                isSelected -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Text(
            text = text,
            color = when {
                isSelected -> MaterialTheme.colorScheme.onPrimary
                else -> MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

@Preview
@Composable
private fun PreviewSelectionButtonSelected() {
    OrarUbbFmiTheme {
        SelectionButton(
            text = "Master",
            isSelected = true,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun PreviewSelectionButtonUnselected() {
    OrarUbbFmiTheme {
        SelectionButton(
            text = "Master",
            isSelected = false,
            onClick = {}
        )
    }
}