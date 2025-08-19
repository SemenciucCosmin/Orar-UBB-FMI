package com.ubb.fmi.orar.feature.timetable.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.data.core.model.Frequency
import com.ubb.fmi.orar.ui.catalog.components.TimetableFrequencyTab
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_left_arrow
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    selectedFrequency: Frequency? = null,
    onFrequencyClick: (Frequency) -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(Res.drawable.ic_left_arrow),
                    contentDescription = null,
                )
            }
        },
        title = {
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = title
            )
        },
        actions = {
            selectedFrequency?.let {
                TimetableFrequencyTab(
                    selectedFrequency = selectedFrequency,
                    onFrequencyClick = onFrequencyClick
                )
            }
        }
    )
}