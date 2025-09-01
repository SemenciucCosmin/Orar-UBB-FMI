package com.ubb.fmi.orar.ui.catalog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.ui.catalog.model.Chip
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun <T>ChipSelectionRow(
    chips: List<Chip<T>>,
    selectedChipId: T?,
    onClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Pds.spacing.Small),
        contentPadding = contentPadding
    ) {
        items(chips) { chip ->
            FilterChip(
                shape = shape,
                selected = chip.id == selectedChipId,
                onClick = { onClick(chip.id) },
                label = { Text(text = chip.label) }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewChipSelectionRow() {
    OrarUbbFmiTheme {
        ChipSelectionRow(
            chips = List(3) { Chip(it, "Chip $it") },
            selectedChipId = 0,
            shape = MaterialTheme.shapes.small,
            onClick = {}
        )
    }
}
