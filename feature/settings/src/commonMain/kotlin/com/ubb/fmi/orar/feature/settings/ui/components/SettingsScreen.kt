package com.ubb.fmi.orar.feature.settings.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemClickable
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_change_configuration
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_contact_me_at
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_developed_by
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_settings
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_theme
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_version
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Screen for all settings options.
 * Theme and configuration change
 */
@Composable
fun SettingsScreen(
    appVersion: String?,
    onBack: () -> Unit,
    onChangeConfigurationClick: () -> Unit,
    onThemeClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.lbl_settings),
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
            modifier = Modifier
                .padding(paddingValues)
                .padding(Pds.spacing.Medium)
        ) {
            ListItemClickable(
                headline = stringResource(Res.string.lbl_change_configuration),
                onClick = onChangeConfigurationClick
            )

            ListItemClickable(
                headline = stringResource(Res.string.lbl_theme),
                onClick = onThemeClick
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(
                verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                appVersion?.let {
                    Text(
                        text = stringResource(Res.string.lbl_version, appVersion),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Text(
                    text = stringResource(Res.string.lbl_developed_by),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium
                )

                RepositoryHyperlinkText()

                Row(horizontalArrangement = Arrangement.spacedBy(Pds.spacing.Medium)) {
                    ReportIssueHyperlinkText()

                    LeaveFeedbackHyperlinkText()
                }

                Text(
                    text = stringResource(Res.string.lbl_contact_me_at),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsScreen() {
    OrarUbbFmiTheme {
        SettingsScreen(
            appVersion = "1.0.0",
            onBack = {},
            onChangeConfigurationClick = {},
            onThemeClick = {},
        )
    }
}
