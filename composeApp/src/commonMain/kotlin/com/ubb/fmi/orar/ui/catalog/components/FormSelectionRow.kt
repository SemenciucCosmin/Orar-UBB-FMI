package com.ubb.fmi.orar.ui.catalog.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import com.ubb.fmi.orar.ui.catalog.model.Chip
import com.ubb.fmi.orar.ui.catalog.model.FormSelectionItem
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun <T> FormSelectionRow(
    headline: String,
    items: List<FormSelectionItem<T>>,
    selectedItemId: T?,
    onClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
) {
    Column(modifier = modifier) {
        Text(
            text = headline,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        ChipSelectionRow(
            chips = items.map { Chip(it.id, it.label) },
            selectedChipId = selectedItemId,
            shape = shape,
            onClick = onClick
        )
    }
}

@Preview
@Composable
private fun PreviewFormSelectionRow() {
    OrarUbbFmiTheme {
        FormSelectionRow(
            headline = "Items",
            items = List(3) { FormSelectionItem(it, "Item $it") },
            selectedItemId = 0,
            shape = MaterialTheme.shapes.small,
            onClick = {}
        )
    }
}
