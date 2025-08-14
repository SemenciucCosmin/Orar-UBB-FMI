package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.ui.catalog.extensions.conditional
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.check
import org.jetbrains.compose.resources.painterResource

@Composable
fun <T> FormListItem(
    headlineLabel: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    overLineLabel: String? = null,
    underlineItems: List<FormInputItem<T>>? = null,
    selectedUnderlineItemId: T? = null,
    onUnderlineItemClick: (T) -> Unit = {},
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.conditional(isSelected) {
            border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    overLineLabel?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Text(
                        text = headlineLabel,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }

                if (isSelected) {
                    Icon(
                        painter = painterResource(Res.drawable.check),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            AnimatedVisibility(visible = isSelected && underlineItems != null) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    HorizontalDivider()

                    if (underlineItems != null) {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            underlineItems.forEach { item ->
                                InputChip(
                                    selected = item.id == selectedUnderlineItemId,
                                    onClick = { onUnderlineItemClick(item.id) },
                                    label = { Text(text = item.label) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
