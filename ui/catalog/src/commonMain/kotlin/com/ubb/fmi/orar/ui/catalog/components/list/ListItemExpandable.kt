package com.ubb.fmi.orar.ui.catalog.components.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.ui.catalog.extensions.conditional
import com.ubb.fmi.orar.ui.catalog.model.Chip
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_check
import orar_ubb_fmi.ui.catalog.generated.resources.ic_study_line
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable that displays an expandable list item with a headline, optional overline,
 * leading icon, and expandable content.
 *
 * @param headline The main text displayed in the list item.
 * @param isSelected Indicates whether the item is currently selected (expanded).
 * @param onClick Callback invoked when the item is clicked to toggle expansion.
 * @param expandedContent Composable content to display when the item is expanded.
 * @param modifier Modifier to be applied to the card.
 * @param textAlign Text alignment for the headline.
 * @param overline Optional text displayed above the headline.
 * @param leadingIcon Optional icon displayed at the start of the item.
 */
@Composable
fun ListItemExpandable(
    headline: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    expandedContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    overline: String? = null,
    leadingIcon: Painter? = null,
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
            ListRow(
                headline = headline,
                textAlign = textAlign,
                overline = overline,
                leadingIcon = leadingIcon,
                trailingIcon = painterResource(Res.drawable.ic_check).takeIf { isSelected },
                trailingIconSize = Pds.icon.Small
            )

            AnimatedVisibility(visible = isSelected) {
                Column(verticalArrangement = Arrangement.spacedBy(Pds.spacing.Small)) {
                    HorizontalDivider()
                    expandedContent()
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewListItemExpandableV1() {
    OrarUbbFmiTheme {
        ListItemExpandable(
            headline = "Headline",
            isSelected = true,
            leadingIcon = painterResource(Res.drawable.ic_study_line),
            onClick = {},
            expandedContent = {
                repeat(3) {
                    ListItemClickable(
                        headline = "Headline $it",
                        onClick = {}
                    )
                }
            },
        )
    }
}

@Preview
@Composable
private fun PreviewListItemExpandableV2() {
    OrarUbbFmiTheme {
        ListItemExpandable(
            headline = "Headline",
            isSelected = true,
            leadingIcon = painterResource(Res.drawable.ic_study_line),
            onClick = {},
            expandedContent = {
                ChipSelectionRow(
                    chips = List(3) { Chip(it, "Chip $it") },
                    selectedChipId = 0,
                    shape = MaterialTheme.shapes.small,
                    onClick = {}
                )
            },
        )
    }
}
