package com.ubb.fmi.orar.feature.notifications.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.ui.catalog.components.custom.AlertDialog
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_cancel
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_notification_permission_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_open_settings
import org.jetbrains.compose.resources.stringResource

/**
 * Dialog explaining that notifications need the system permission, offering to open the
 * settings screen where it can be granted.
 *
 * @param onOpenSettingsClick Callback invoked when the user chooses to open settings.
 * @param onDismiss Callback invoked when the dialog is cancelled or dismissed.
 */
@Composable
fun NotificationPermissionDialog(
    onOpenSettingsClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        text = stringResource(Res.string.lbl_notification_permission_message),
        positiveButtonText = stringResource(Res.string.lbl_open_settings),
        negativeButtonText = stringResource(Res.string.lbl_cancel),
        onPositiveClick = onOpenSettingsClick,
        onNegativeClick = onDismiss,
    )
}

@Preview
@Composable
private fun PreviewNotificationPermissionDialog() {
    OrarUbbFmiTheme {
        NotificationPermissionDialog(
            onOpenSettingsClick = {},
            onDismiss = {},
        )
    }
}
