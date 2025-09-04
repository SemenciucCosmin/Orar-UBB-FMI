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
import com.ubb.fmi.orar.data.timetable.model.TimetableClass
import com.ubb.fmi.orar.ui.catalog.components.state.StateScaffold
import com.ubb.fmi.orar.ui.catalog.model.ClassType
import com.ubb.fmi.orar.ui.catalog.model.Day
import com.ubb.fmi.orar.ui.catalog.model.Frequency
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
                isError = false,
                classes = List(10) {
                    TimetableClass(
                        id = "$it",
                        day = Day.entries.random().id,
                        startHour = "12",
                        endHour = "14",
                        frequencyId = Frequency.entries.random().id,
                        room = "Room $it",
                        field = "Field $it",
                        participant = "Participant $it",
                        classType = ClassType.entries.random().id,
                        ownerId = "$it",
                        groupId = "$it",
                        ownerTypeId = "$it",
                        subject = "Subject $it",
                        teacher = "Teacher $it",
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
                isError = false,
                classes = List(10) {
                    TimetableClass(
                        id = "$it",
                        day = Day.entries.random().id,
                        startHour = "12",
                        endHour = "14",
                        frequencyId = Frequency.entries.random().id,
                        room = "Room $it",
                        field = "Field $it",
                        participant = "Participant $it",
                        classType = ClassType.entries.random().id,
                        ownerId = "$it",
                        groupId = "$it",
                        ownerTypeId = "$it",
                        subject = "Subject $it",
                        teacher = "Teacher $it",
                        isVisible = true,
                        configurationId = "20241"
                    )
                }.toImmutableList()
            )
        )
    }
}