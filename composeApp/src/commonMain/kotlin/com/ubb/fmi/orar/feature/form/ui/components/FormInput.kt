package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun <T> FormInput(
    title: String,
    items: List<FormInputItem<T>>,
    selectedItemId: T?,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                InputChip(
                    selected = item.id == selectedItemId,
                    onClick = { onItemClick(item.id) },
                    label = {
                        Text(text = item.label)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewFormInput() {
    OrarUbbFmiTheme {
        FormInput(
            title = "Title",
            selectedItemId = 1,
            onItemClick = {},
            items = listOf(
                FormInputItem(id = 1, label = "Label 1"),
                FormInputItem(id = 2, label = "Label 2"),
                FormInputItem(id = 3, label = "Label 3"),
            )
        )
    }
}

data class FormInputItem<T>(
    val id: T,
    val label: String,
)
