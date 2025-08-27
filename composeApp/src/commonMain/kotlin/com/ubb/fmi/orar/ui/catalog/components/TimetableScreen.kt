package com.ubb.fmi.orar.ui.catalog.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.ui.catalog.model.TimetableListItem
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.TimetableUiState
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.TimetableUiState.Companion.timetableListItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(
    uiState: TimetableUiState,
    onRetryClick: () -> Unit,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit = {},
    onItemVisibilityChange: (TimetableListItem.Class) -> Unit = {}
) {
    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                ProgressOverlay(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                )
            }

            uiState.isError -> {
                FailureState(
                    onRetry = onRetryClick,
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(12.dp),
                ) {
                    items(
                        uiState.timetableListItems,
                        key = { timetableItem ->
                            when(timetableItem) {
                                is TimetableListItem.Divider -> timetableItem.day
                                is TimetableListItem.Class -> timetableItem.id
                            }
                        }
                    ) { timetableItem ->
                        when (timetableItem) {
                            is TimetableListItem.Divider -> {
                                TimetableListDivider(
                                    modifier = Modifier.animateItem(),
                                    text = timetableItem.day,
                                )
                            }

                            is TimetableListItem.Class -> {
                                Row(
                                    modifier = Modifier.animateItem(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AnimatedVisibility(uiState.isEditModeOn) {
                                        TimetableVisibilityButton(
                                            isVisible = timetableItem.isVisible,
                                            onClick = { onItemVisibilityChange(timetableItem) }
                                        )
                                    }

                                    TimetableListItem(
                                        startHour = timetableItem.startHour,
                                        endHour = timetableItem.endHour,
                                        subject = timetableItem.subject,
                                        classType = timetableItem.classType,
                                        participant = timetableItem.participant,
                                        teacher = timetableItem.teacher,
                                        room = timetableItem.room,
                                        enabled = timetableItem.isVisible,
                                        expanded = !uiState.isEditModeOn
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
