package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_hide
import orar_ubb_fmi.ui.catalog.generated.resources.ic_show
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable that displays a button to toggle the visibility of a timetable.
 *
 * @param isVisible Indicates whether the timetable is currently visible.
 * @param onClick Callback invoked when the button is clicked to toggle visibility.
 */
@Composable
fun TimetableVisibilityButton(
    isVisible: Boolean,
    onClick: () -> Unit,
) {
    IconToggleButton(
        checked = isVisible,
        onCheckedChange = { onClick() },
    ) {
        Icon(
            modifier = Modifier.size(Pds.icon.SMedium),
            contentDescription = null,
            painter = when {
                isVisible -> painterResource(Res.drawable.ic_show)
                else -> painterResource(Res.drawable.ic_hide)
            }
        )
    }
}

@Preview
@Composable
private fun PreviewTimetableVisibilityButtonVisible() {
    OrarUbbFmiTheme {
        TimetableVisibilityButton(
            isVisible = true,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun PreviewTimetableVisibilityButtonNotVisible() {
    OrarUbbFmiTheme {
        TimetableVisibilityButton(
            isVisible = false,
            onClick = {}
        )
    }
}
