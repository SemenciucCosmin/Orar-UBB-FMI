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
import com.ubb.fmi.orar.data.timetable.model.Activity
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.Host
import com.ubb.fmi.orar.data.timetable.model.Location
import com.ubb.fmi.orar.data.timetable.model.Participant
import com.ubb.fmi.orar.ui.catalog.components.state.StateScaffold
import com.ubb.fmi.orar.ui.catalog.extensions.labelRes
import com.ubb.fmi.orar.ui.catalog.model.TimetableListItem
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.TimetableUiState
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.TimetableUiState.Companion.timetableListItems
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.toImmutableList
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_hour
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable that displays the timetable screen with a list of timetable items.
 * @param uiState The current state of the timetable UI.
 * @param onRetryClick Callback invoked when the retry button is clicked.
 * @param topBar Composable for the top bar of the screen.
 * @param bottomBar Composable for the bottom bar of the screen (optional).
 * @param onItemVisibilityChange Callback invoked when the visibility of a timetable item changes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(
    uiState: TimetableUiState,
    onRetryClick: () -> Unit,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit = {},
    onItemVisibilityChange: (TimetableListItem.Event) -> Unit = {},
) {
    StateScaffold(
        isLoading = uiState.isLoading,
        errorStatus = uiState.errorStatus,
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
                        is TimetableListItem.Event -> timetableItem.id
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

                    is TimetableListItem.Event -> {
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
                                enabled = timetableItem.isVisible,
                                expanded = !uiState.isEditModeOn,
                                location = timetableItem.location,
                                title = timetableItem.title,
                                type = timetableItem.type,
                                participantName = timetableItem.participantName,
                                hostName = timetableItem.hostName,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewTimetableScreen() {
    OrarUbbFmiTheme {
        TimetableScreen(
            onRetryClick = {},
            topBar = {},
            bottomBar = {},
            onItemVisibilityChange = {},
            uiState = TimetableUiState(
                title = "",
                studyLevel = null,
                group = null,
                selectedFrequency = Frequency.WEEK_1,
                isEditModeOn = false,
                isLoading = false,
                errorStatus = null,
                events = List(10) {
                    Event(
                        id = "$it",
                        day = Day.entries.random(),
                        startHour = 12,
                        endHour = 14,
                        frequency = Frequency.entries.random(),
                        location = Location(
                            id = "$it",
                            name = "Room $it",
                            address = "Str. Street nr. $it"
                        ),
                        participant = Participant(
                            id = "$it",
                            name = "Participant $it",
                        ),
                        type = EventType.entries.random(),
                        ownerId = "$it",
                        activity = Activity(
                            id = "$it",
                            name = "Activity $it"
                        ),
                        host = Host(
                            id = "$it",
                            name = "Host $it"
                        ),
                        isVisible = true,
                        configurationId = "20241"
                    )
                }.toImmutableList()
            )
        )
    }
}

@Preview
@Composable
private fun PreviewTimetableScreenEditMode() {
    OrarUbbFmiTheme {
        TimetableScreen(
            onRetryClick = {},
            topBar = {},
            bottomBar = {},
            onItemVisibilityChange = {},
            uiState = TimetableUiState(
                title = "",
                studyLevel = null,
                group = null,
                selectedFrequency = Frequency.WEEK_1,
                isEditModeOn = true,
                isLoading = false,
                errorStatus = null,
                events = List(10) {
                    Event(
                        id = "$it",
                        day = Day.entries.random(),
                        startHour = 12,
                        endHour = 14,
                        frequency = Frequency.entries.random(),
                        location = Location(
                            id = "$it",
                            name = "Room $it",
                            address = "Str. Street nr. $it"
                        ),
                        participant = Participant(
                            id = "$it",
                            name = "Participant $it",
                        ),
                        type = EventType.entries.random(),
                        ownerId = "$it",
                        activity = Activity(
                            id = "$it",
                            name = "Activity $it"
                        ),
                        host = Host(
                            id = "$it",
                            name = "Host $it"
                        ),
                        isVisible = true,
                        configurationId = "20241"
                    )
                }.toImmutableList()
            )
        )
    }
}