package com.ubb.fmi.orar.feature.notifications.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.feature.notifications.ui.viewmodel.model.NotificationsUiState
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemClickable
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_notifications
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_notifications_enabled
import org.jetbrains.compose.resources.stringResource

/**
 * Screen for configuring event notifications: a global on/off switch and, while enabled, how
 * many minutes in advance of an event's start its notification should fire.
 */
@Composable
fun NotificationsScreen(
    uiState: NotificationsUiState,
    onBack: () -> Unit,
    onNotificationsEnabledChange: (Boolean) -> Unit,
    onNotificationAdvanceMinutesChange: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.lbl_notifications),
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
                headline = stringResource(Res.string.lbl_notifications_enabled),
                onClick = { onNotificationsEnabledChange(!uiState.notificationsEnabled) }
            ) {
                Switch(
                    checked = uiState.notificationsEnabled,
                    onCheckedChange = onNotificationsEnabledChange
                )
            }

            NotificationsTimeListItem(
                advanceMinutes = uiState.notificationAdvanceMinutes,
                advanceHours = uiState.notificationAdvanceHours,
                enabled = uiState.notificationsEnabled,
                onSetMinutes = onNotificationAdvanceMinutesChange
            )
        }
    }
}

@Preview
@Composable
private fun PreviewNotificationsScreen() {
    OrarUbbFmiTheme {
        NotificationsScreen(
            uiState = NotificationsUiState(),
            onBack = {},
            onNotificationsEnabledChange = {},
            onNotificationAdvanceMinutesChange = {},
        )
    }
}
