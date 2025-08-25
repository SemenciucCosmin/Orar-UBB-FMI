package com.ubb.fmi.orar.feature.teachertimetable.ui.components

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
import com.ubb.fmi.orar.data.teachers.model.TeacherTitle
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.feature.teachertimetable.ui.viewmodel.model.TeacherTimetableUiState
import com.ubb.fmi.orar.feature.teachertimetable.ui.viewmodel.model.TeacherTimetableUiState.Companion.timetableItems
import com.ubb.fmi.orar.feature.timetable.ui.components.TimetableListDivider
import com.ubb.fmi.orar.feature.timetable.ui.components.TimetableListItem
import com.ubb.fmi.orar.feature.timetable.ui.components.TimetableTopBar
import com.ubb.fmi.orar.feature.timetable.ui.model.TimetableItem
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherTimetableScreen(
    uiState: TeacherTimetableUiState,
    onFrequencyClick: (Frequency) -> Unit,
    onRetryClick: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            if (!uiState.isLoading && !uiState.isError) {
                TimetableTopBar(
                    onBack = onBack,
                    selectedFrequency = uiState.selectedFrequency,
                    onFrequencyClick = onFrequencyClick,
                    title = when {
                        uiState.teacher == null -> String.BLANK

                        else -> {
                            val title = TeacherTitle.getById(uiState.teacher.titleId)
                            "${title.label} ${uiState.teacher.name}"
                        }
                    }
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

            uiState.isError || uiState.teacher == null -> {
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
