package com.ubb.fmi.orar.feature.usertimetable.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.ui.catalog.components.timetable.TimetableFrequencyTab
import com.ubb.fmi.orar.ui.catalog.model.Frequency
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.feature.user_timetable.generated.resources.Res
import orar_ubb_fmi.feature.user_timetable.generated.resources.ic_edit
import orar_ubb_fmi.feature.user_timetable.generated.resources.ic_settings
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Top bar for the User Timetable screen.
 * This bar includes a settings button, an edit toggle button,
 * and a frequency selection tab.
 * @param isLoading Indicates if the data is loading
 * @param isError Indicates if the data load failed
 * @param isEditModeOn Indicates if the edit mode is active.
 * @param selectedFrequency The currently selected frequency.
 * @param onSettingsClick Callback for when the settings button is clicked.
 * @param onEditClick Callback for when the edit toggle button is clicked.
 * @param onFrequencyClick Callback for when a frequency is selected.
 * @param modifier Optional modifier for styling.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTimetableTopBar(
    isLoading: Boolean,
    isError: Boolean,
    isEditModeOn: Boolean,
    selectedFrequency: Frequency,
    onSettingsClick: () -> Unit,
    onEditClick: () -> Unit,
    onFrequencyClick: (Frequency) -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    modifier = Modifier.size(Pds.icon.SMedium),
                    painter = painterResource(Res.drawable.ic_settings),
                    contentDescription = null,
                )
            }
        },
        title = {
            if (!isError && !isLoading) {
                FilledIconToggleButton(
                    checked = isEditModeOn,
                    onCheckedChange = { onEditClick() }
                ) {
                    Icon(
                        modifier = Modifier.size(Pds.icon.Medium),
                        painter = painterResource(Res.drawable.ic_edit),
                        contentDescription = null,
                    )
                }
            }
        },
        actions = {
            if (!isError && !isLoading) {
                TimetableFrequencyTab(
                    selectedFrequency = selectedFrequency,
                    onFrequencyClick = onFrequencyClick
                )
            }
        }
    )
}

@Preview
@Composable
private fun PreviewUserTimetableTopBar() {
    OrarUbbFmiTheme {
        UserTimetableTopBar(
            isLoading = false,
            isError = false,
            isEditModeOn = false,
            selectedFrequency = Frequency.WEEK_1,
            onSettingsClick = {},
            onEditClick = {},
            onFrequencyClick = {},
        )
    }
}
