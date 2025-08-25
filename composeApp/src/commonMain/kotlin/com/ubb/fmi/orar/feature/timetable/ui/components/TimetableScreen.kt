package com.ubb.fmi.orar.feature.timetable.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.data.core.model.Frequency
import com.ubb.fmi.orar.feature.timetable.ui.model.TimetableListItem
import com.ubb.fmi.orar.feature.timetable.ui.viewmodel.model.TimetableUiState
import com.ubb.fmi.orar.feature.timetable.ui.viewmodel.model.TimetableUiState.Companion.timetableListItems
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(
    uiState: TimetableUiState,
    onFrequencyClick: (Frequency) -> Unit,
    onRetryClick: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            uiState.timetable?.let { timetable ->
                TimetableTopBar(
                    title = timetable.title,
                    subtitle = timetable.subtitle,
                    selectedFrequency = uiState.selectedFrequency,
                    onFrequencyClick = onFrequencyClick,
                    onBack = onBack
                )
            }
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                ProgressOverlay(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                )
            }

            uiState.isError || uiState.timetable == null -> {
                FailureState(
                    onRetry = onRetryClick,
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                )
            }

            else -> {
                Column(modifier = Modifier.padding(paddingValues)) {
                    LazyColumn(
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(uiState.timetableListItems) { timetableItem ->
                            when (timetableItem) {
                                is TimetableListItem.Divider -> {
                                    TimetableListDivider(text = timetableItem.day)
                                }

                                is TimetableListItem.Class -> {
                                    TimetableListItem(
                                        startHour = timetableItem.startHour,
                                        endHour = timetableItem.endHour,
                                        subject = timetableItem.subject,
                                        classType = timetableItem.classType,
                                        participant = timetableItem.participant,
                                        teacher = timetableItem.teacher,
                                        room = timetableItem.room,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
