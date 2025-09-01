package com.ubb.fmi.orar.ui.catalog.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.ui.catalog.extensions.conditional
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_check
import org.jetbrains.compose.resources.painterResource

@Composable
fun <T> FormListItem(
    headlineLabel: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    overLineLabel: String? = null,
    underLineLabel: String? = null,
    underlineItems: List<FormInputItem<T>>? = null,
    selectedUnderlineItemId: T? = null,
    onUnderlineItemClick: (T) -> Unit = {},
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.conditional(isSelected) {
            border(
                width = Pds.stroke.Medium,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(Pds.spacing.SMedium),
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.Small)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(Pds.spacing.XXSmall)
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

                    underLineLabel?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                if (isSelected) {
                    Icon(
                        modifier = Modifier.size(Pds.icon.SMedium),
                        painter = painterResource(Res.drawable.ic_check),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            AnimatedVisibility(visible = isSelected && underlineItems != null) {
                Column(verticalArrangement = Arrangement.spacedBy(Pds.spacing.Small)) {
                    HorizontalDivider()

                    if (underlineItems != null) {
                        Row(horizontalArrangement = Arrangement.spacedBy(Pds.spacing.Medium)) {
                            underlineItems.forEach { item ->
                                FilterChip(
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
