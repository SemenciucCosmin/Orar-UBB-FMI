package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
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
        BasicAlertDialog(
            onDismissRequest = { isDialogOpen = false },
            properties = DialogProperties(),
            content = {
                Surface(shape = MaterialTheme.shapes.medium) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(Pds.spacing.Medium)
                            .width(IntrinsicSize.Max)
                    ) {
                        Text(
                            text = stringResource(Res.string.lbl_remove_personal_event_message),
                            style = MaterialTheme.typography.titleMedium
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                Pds.spacing.Medium,
                                Alignment.End
                            )
                        ) {
                            TextButton(onClick = { isDialogOpen = false }) {
                                Text(text = stringResource(Res.string.lbl_cancel))
                            }

                            TextButton(
                                onClick = {
                                    onRemove()
                                    isDialogOpen = false
                                }
                            ) {
                                Text(text = stringResource(Res.string.lbl_ok))
                            }
                        }
                    }
                }
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
