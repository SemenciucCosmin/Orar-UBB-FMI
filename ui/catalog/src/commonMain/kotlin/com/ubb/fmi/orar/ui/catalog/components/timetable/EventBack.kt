package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.ui.catalog.components.custom.AlertDialog
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_plus_circle
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_add_event_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_cancel
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_no_event_details
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_ok
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Composable for the back side of an Event card, showing its details, or an empty state
 * message when there are none.
 *
 * @param enabled Whether the add button responds to clicks, false while the back isn't showing.
 */
@Composable
fun EventBack(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onAddClick: (() -> Unit)? = null,
) {
    var isDialogOpen by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(intrinsicSize = IntrinsicSize.Max)
    ) {
        onAddClick?.let {
            IconButton(
                enabled = enabled,
                onClick = { isDialogOpen = true }
            ) {
                Icon(
                    modifier = Modifier.size(Pds.icon.SMedium),
                    contentDescription = null,
                    painter = painterResource(Res.drawable.ic_plus_circle)
                )
            }

            VerticalDivider()
        }

        Text(
            text = when {
                text.isBlank() -> stringResource(Res.string.lbl_no_event_details)
                else -> text
            },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(Pds.spacing.SMedium)
        )
    }

    if (isDialogOpen) {
        AlertDialog(
            text = stringResource(Res.string.lbl_add_event_message),
            positiveButtonText = stringResource(Res.string.lbl_ok),
            negativeButtonText = stringResource(Res.string.lbl_cancel),
            onNegativeClick = { isDialogOpen = false },
            onPositiveClick = {
                onAddClick?.invoke()
                isDialogOpen = false
            }
        )
    }
}

@Preview
@Composable
private fun PreviewEventBack() {
    OrarUbbFmiTheme {
        EventBack(
            text = "Str. Teodor Mihali nr. 38-40",
            onAddClick = {}
        )
    }
}

@Preview
@Composable
private fun PreviewEmptyEventBack() {
    OrarUbbFmiTheme {
        EventBack(
            text = "",
            onAddClick = {}
        )
    }
}
