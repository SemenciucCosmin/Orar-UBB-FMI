package com.ubb.fmi.orar.feature.usertimetable.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.feature.timetable.ui.model.Frequency
import com.ubb.fmi.orar.ui.catalog.components.TimetableFrequencyTab
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_left_arrow
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTimetableTopBar(
    modifier: Modifier = Modifier,
    selectedFrequency: Frequency,
    onSettingsClick: () -> Unit,
    onEditClick: () -> Unit,
    onFrequencyClick: (Frequency) -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    painter = painterResource(Res.drawable.ic_left_arrow),
                    contentDescription = null,
                )
            }
        },
        title = {
            IconButton(onClick = onEditClick) {
                Icon(
                    painter = painterResource(Res.drawable.ic_left_arrow),
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
