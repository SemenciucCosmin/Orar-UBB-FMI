package com.ubb.fmi.orar.feature.dialogs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.feature.dialogs.ui.model.UpdateHighlight
import com.ubb.fmi.orar.ui.catalog.components.PrimaryButton
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_app_update_announcement_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_got_it
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_support
import org.jetbrains.compose.resources.stringResource

/**
 * One time dialog presenting the highlights of a new app version, asking users to report
 * issues and share improvement ideas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppUpdateAnnouncement(
    onDismiss: () -> Unit,
    onSupportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = Pds.spacing.XSmall,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(Pds.spacing.Large)
            ) {
                AppUpdateAnnouncementHeader()

                Column(verticalArrangement = Arrangement.spacedBy(Pds.spacing.SMedium)) {
                    UpdateHighlight.entries.forEach { UpdateHighlightRow(it) }
                }

                Text(
                    text = stringResource(Res.string.lbl_app_update_announcement_message),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(Pds.spacing.SMedium)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    PrimaryButton(
                        text = stringResource(Res.string.lbl_got_it),
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextButton(onClick = onSupportClick) {
                        Text(text = stringResource(Res.string.lbl_support))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAppUpdateAnnouncement() {
    OrarUbbFmiTheme {
        AppUpdateAnnouncement(
            onDismiss = {},
            onSupportClick = {},
        )
    }
}
