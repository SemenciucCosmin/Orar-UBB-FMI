package com.ubb.fmi.orar.ui.catalog.components.list

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

/**
 * A composable that displays a row of selectable chips with a headline.
 *
 * @param headline The title of the selection row.
 * @param items The list of items to display as chips.
 * @param selectedItemId The ID of the currently selected item, or null if none is selected.
 * @param onClick Callback invoked when a chip is clicked, passing the ID of the clicked item.
 * @param modifier Modifier to be applied to the row.
 * @param shape The shape of the chips.
 */
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
