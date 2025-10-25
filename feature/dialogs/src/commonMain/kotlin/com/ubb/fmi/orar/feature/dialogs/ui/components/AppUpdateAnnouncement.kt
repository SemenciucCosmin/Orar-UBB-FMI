package com.ubb.fmi.orar.feature.dialogs.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.DialogProperties
import com.ubb.fmi.orar.ui.catalog.components.PrimaryButton
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_app_update_announcement_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_app_update_announcement_title
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_got_it
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppUpdateAnnouncement(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        properties = DialogProperties(),
        content = {
            Surface(shape = MaterialTheme.shapes.medium) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(Pds.spacing.Medium)
                ) {
                    Text(
                        text = stringResource(Res.string.lbl_app_update_announcement_title),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.lbl_app_update_announcement_message),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Start
                    )

                    PrimaryButton(
                        text = stringResource(Res.string.lbl_got_it),
                        onClick = onDismiss
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun PreviewAppUpdateAnnouncement() {
    OrarUbbFmiTheme {
        AppUpdateAnnouncement(
            onDismiss = {}
        )
    }
}
