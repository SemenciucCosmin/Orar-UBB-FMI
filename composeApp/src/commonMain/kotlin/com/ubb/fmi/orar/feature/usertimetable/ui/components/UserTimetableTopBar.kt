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
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_edit
import orar_ubb_fmi.composeapp.generated.resources.ic_settings
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTimetableTopBar(
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
        },
        actions = {
            TimetableFrequencyTab(
                selectedFrequency = selectedFrequency,
                onFrequencyClick = onFrequencyClick
            )
        }
    )
}
