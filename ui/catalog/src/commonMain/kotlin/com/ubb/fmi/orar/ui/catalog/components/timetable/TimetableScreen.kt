package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.ui.catalog.components.state.StateScaffold
import com.ubb.fmi.orar.ui.catalog.model.TimetableListItem
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.TimetableUiState
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.TimetableUiState.Companion.timetableListItems
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private const val FIRST_INDEX = 0
private const val SCROLL_INDEX_OFFSET = 3

/**
 * A composable that displays the timetable screen with a list of timetable items.
 * @param uiState The current state of the timetable UI.
 * @param onRetryClick Callback invoked when the retry button is clicked.
 * @param topBar Composable for the top bar of the screen.
 * @param bottomBar Composable for the bottom bar of the screen (optional).
 * @param onItemVisibilityChange Callback invoked when the visibility of a timetable item changes.
 * @param selectedEventId Optional event ID to scroll to and animate when the screen loads.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(
    uiState: TimetableUiState,
    onRetryClick: () -> Unit,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit = {},
    onItemVisibilityChange: (TimetableListItem.Event) -> Unit = {},
    onItemNotificationChange: (TimetableListItem.Event) -> Unit = {},
    onRemoveItem: (TimetableListItem.Event) -> Unit = {},
    onAddItem: ((String) -> Unit)? = null,
    selectedEventId: String? = null,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var isScrollDone by remember { mutableStateOf(false) }

    LaunchedEffect(selectedEventId, uiState.timetableListItems) {
        if (selectedEventId == null || uiState.timetableListItems.isEmpty()) return@LaunchedEffect
        val itemIndex = uiState.timetableListItems.indexOfFirst { item ->
            item is TimetableListItem.Event && item.id == selectedEventId
        }.let {
            when {
                it - SCROLL_INDEX_OFFSET >= FIRST_INDEX -> it - SCROLL_INDEX_OFFSET
                else -> FIRST_INDEX
            }
        }

        if (itemIndex >= FIRST_INDEX) {
            isScrollDone = false
            coroutineScope.launch {
                delay(500.milliseconds)
                listState.animateScrollToItem(itemIndex)
            }.invokeOnCompletion { isScrollDone = true }
        }
    }

    StateScaffold(
        isLoading = uiState.isLoading,
        isEmpty = uiState.isEmpty,
        errorStatus = uiState.errorStatus,
        onRetryClick = onRetryClick,
        topBar = topBar,
        bottomBar = bottomBar
    ) { paddingValues ->
        EventsList(
            modifier = Modifier.padding(paddingValues),
            items = uiState.timetableListItems,
            isEditModeOn = uiState.isEditModeOn,
            listState = listState,
            onVisibleClick = onItemVisibilityChange,
            onNotificationClick = onItemNotificationChange,
            onRemoveClick = onRemoveItem,
            onAddItem = onAddItem,
        )
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
                        startMinute = 0,
                        endHour = 14,
                        endMinute = 0,
                        frequency = Frequency.entries.random(),
                        location = "A304",
                        participant = "Participant $it",
                        type = EventType.entries.random(),
                        activity = "Activity $it",
                        caption = "Caption $it",
                        details = "Details $it",
                        isVisible = true,
                        isNotificationOn = false,
                        configurationId = "20241",
                        ownerId = "$it"
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
            onItemNotificationChange = {},
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
                        startMinute = 0,
                        endHour = 14,
                        endMinute = 0,
                        frequency = Frequency.entries.random(),
                        location = "A304",
                        participant = "Participant $it",
                        type = EventType.entries.random(),
                        activity = "Activity $it",
                        caption = "Caption $it",
                        details = "Details $it",
                        isVisible = true,
                        isNotificationOn = false,
                        configurationId = "20241",
                        ownerId = "$it",
                    )
                }.toImmutableList()
            )
        )
    }
}