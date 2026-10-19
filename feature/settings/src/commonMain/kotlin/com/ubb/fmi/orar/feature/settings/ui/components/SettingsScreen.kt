package com.ubb.fmi.orar.feature.settings.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemClickable
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_about
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_change_configuration
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_colors
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_notifications
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_settings
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_theme
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_version
import org.jetbrains.compose.resources.stringResource

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
    onNotificationsClick: () -> Unit,
    onColorsClick: () -> Unit,
    onAboutClick: () -> Unit,
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

            ListItemClickable(
                headline = stringResource(Res.string.lbl_colors),
                onClick = onColorsClick
            )

            ListItemClickable(
                headline = stringResource(Res.string.lbl_notifications),
                onClick = onNotificationsClick
            )

            ListItemClickable(
                headline = stringResource(Res.string.lbl_about),
                underLine = appVersion?.let { stringResource(Res.string.lbl_version, it) },
                onClick = onAboutClick
            )
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
            onNotificationsClick = {},
            onColorsClick = {},
            onAboutClick = {},
        )
    }
}
