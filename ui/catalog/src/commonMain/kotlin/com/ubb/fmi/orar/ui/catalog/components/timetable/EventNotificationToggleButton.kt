package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_notification_off
import orar_ubb_fmi.ui.catalog.generated.resources.ic_notification_on
import org.jetbrains.compose.resources.painterResource

@Composable
fun EventNotificationToggleButton(
    isChecked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconToggleButton(
        modifier = modifier.size(Pds.icon.Medium),
        checked = isChecked,
        onCheckedChange = { onClick() },
    ) {
        Icon(
            modifier = Modifier.size(Pds.icon.SMedium),
            contentDescription = null,
            painter = when {
                isChecked -> painterResource(Res.drawable.ic_notification_on)
                else -> painterResource(Res.drawable.ic_notification_off)
            }
        )
    }
}

@Preview
@Composable
private fun PreviewEventNotificationToggleButtonOn() {
    OrarUbbFmiTheme {
        EventNotificationToggleButton(
            isChecked = true,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun PreviewEventNotificationToggleButtonOff() {
    OrarUbbFmiTheme {
        EventNotificationToggleButton(
            isChecked = false,
            onClick = {}
        )
    }
}
