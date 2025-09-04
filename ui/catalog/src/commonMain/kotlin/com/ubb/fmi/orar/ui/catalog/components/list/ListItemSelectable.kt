package com.ubb.fmi.orar.ui.catalog.components.list

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.ui.catalog.extensions.conditional
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_check
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A selectable list item that displays a headline, optional overline and underline text,
 * and an optional leading icon. The item can be selected, which highlights it with a border.
 *
 * @param headline The main text displayed in the list item.
 * @param isSelected Indicates whether the item is currently selected.
 * @param onClick Callback invoked when the item is clicked to toggle selection.
 * @param modifier Modifier to be applied to the card.
 * @param textAlign Text alignment for the headline.
 * @param overline Optional text displayed above the headline.
 * @param underline Optional text displayed below the headline.
 * @param leadingIcon Optional icon displayed at the start of the item.
 */
@Composable
fun ListItemSelectable(
    headline: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    overline: String? = null,
    underline: String? = null,
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
        ListRow(
            modifier = Modifier.padding(Pds.spacing.SMedium),
            headline = headline,
            textAlign = textAlign,
            overline = overline,
            underLine = underline,
            leadingIcon = leadingIcon,
            trailingIcon = painterResource(Res.drawable.ic_check).takeIf { isSelected },
            trailingIconSize = Pds.icon.Small
        )
    }
}

@Preview
@Composable
private fun PreviewListItemSelectableV1() {
    OrarUbbFmiTheme {
        ListItemSelectable(
            headline = "Headline",
            isSelected = true,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewListItemSelectableV2() {
    OrarUbbFmiTheme {
        ListItemSelectable(
            headline = "Headline",
            overline = "Overline",
            isSelected = true,
            onClick = {},
        )
    }
}
