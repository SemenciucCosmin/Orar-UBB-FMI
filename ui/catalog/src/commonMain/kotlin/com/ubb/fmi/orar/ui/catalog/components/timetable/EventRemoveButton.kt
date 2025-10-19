package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.ui.catalog.components.custom.AlertDialog
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_trash
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_cancel
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_ok
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_remove_personal_event_message
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable that displays a button to remove an event
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventRemoveButton(
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isDialogOpen by remember { mutableStateOf(false) }

    IconButton(
        modifier = modifier,
        onClick = { isDialogOpen = true }
    ) {
        Icon(
            modifier = Modifier.size(Pds.icon.SMedium),
            contentDescription = null,
            painter = painterResource(Res.drawable.ic_trash)
        )
    }

    if (isDialogOpen) {
        AlertDialog(
            text = stringResource(Res.string.lbl_remove_personal_event_message),
            positiveButtonText = stringResource(Res.string.lbl_ok),
            negativeButtonText = stringResource(Res.string.lbl_cancel),
            onNegativeClick = { isDialogOpen = false },
            onPositiveClick = {
                onRemove()
                isDialogOpen = false
            }
        )
    }
}

@Preview
@Composable
private fun PreviewEventRemoveButton() {
    OrarUbbFmiTheme {
        EventRemoveButton(
            onRemove = {}
        )
    }
}
