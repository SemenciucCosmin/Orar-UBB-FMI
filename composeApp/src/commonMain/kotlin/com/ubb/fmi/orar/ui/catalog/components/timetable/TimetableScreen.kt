package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.ui.catalog.components.state.StateScaffold
import com.ubb.fmi.orar.ui.catalog.model.TimetableListItem
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.TimetableUiState
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.TimetableUiState.Companion.timetableListItems
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.lbl_hour
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(
    uiState: TimetableUiState,
    onRetryClick: () -> Unit,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit = {},
    onItemVisibilityChange: (TimetableListItem.Class) -> Unit = {},
) {
    StateScaffold(
        isLoading = uiState.isLoading,
        isError = uiState.isError,
        onRetryClick = onRetryClick,
        topBar = topBar,
        bottomBar = bottomBar
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
            contentPadding = PaddingValues(Pds.spacing.SMedium),
        ) {
            items(
                uiState.timetableListItems,
                key = { timetableItem ->
                    when (timetableItem) {
                        is TimetableListItem.Divider -> timetableItem.day
                        is TimetableListItem.Class -> timetableItem.id
                    }
                }
            ) { timetableItem ->
                when (timetableItem) {
                    is TimetableListItem.Divider -> {
                        TimetableListDivider(
                            modifier = Modifier.animateItem(),
                            text = stringResource(timetableItem.day.labelRes),
                        )
                    }

                    is TimetableListItem.Class -> {
                        Row(
                            modifier = Modifier.animateItem(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                Pds.spacing.SMedium
                            )
                        ) {
                            AnimatedVisibility(uiState.isEditModeOn) {
                                TimetableVisibilityButton(
                                    isVisible = timetableItem.isVisible,
                                    onClick = { onItemVisibilityChange(timetableItem) }
                                )
                            }

                            TimetableListItem(
                                startHour = stringResource(
                                    Res.string.lbl_hour,
                                    timetableItem.startHour
                                ),
                                endHour = stringResource(
                                    Res.string.lbl_hour,
                                    timetableItem.endHour
                                ),
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
