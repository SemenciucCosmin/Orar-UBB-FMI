package com.ubb.fmi.orar.feature.roomtimetable.ui.components

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
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.feature.roomtimetable.ui.viewmodel.model.RoomTimetableUiState
import com.ubb.fmi.orar.feature.roomtimetable.ui.viewmodel.model.RoomTimetableUiState.Companion.timetableItems
import com.ubb.fmi.orar.feature.subjecttimetable.ui.viewmodel.model.SubjectTimetableUiState
import com.ubb.fmi.orar.feature.subjecttimetable.ui.viewmodel.model.SubjectTimetableUiState.Companion.timetableItems
import com.ubb.fmi.orar.feature.timetable.ui.components.TimetableListDivider
import com.ubb.fmi.orar.feature.timetable.ui.components.TimetableListItem
import com.ubb.fmi.orar.feature.timetable.ui.components.TimetableTopBar
import com.ubb.fmi.orar.feature.timetable.ui.model.TimetableItem
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomTimetableScreen(
    uiState: RoomTimetableUiState,
    onFrequencyClick: (Frequency) -> Unit,
    onRetryClick: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TimetableTopBar(
                onBack = onBack,
                selectedFrequency = uiState.selectedFrequency,
                onFrequencyClick = onFrequencyClick,
                title = uiState.room?.name ?: String.BLANK
            )
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

            uiState.isError || uiState.room == null -> {
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
                        items(uiState.timetableItems) { timetableItem ->
                            when (timetableItem) {
                                is TimetableItem.Divider -> {
                                    TimetableListDivider(text = timetableItem.day)
                                }

                                is TimetableItem.Class -> {
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
