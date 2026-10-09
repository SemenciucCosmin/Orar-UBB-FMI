package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

private const val FIRST_INDEX = 0
private const val SCROLL_INDEX_OFFSET = 3
private const val SECONDS_PER_MINUTE = 60
private val SCROLL_START_DELAY = 500.milliseconds

/**
 * A composable that displays the timetable screen with a list of timetable items.
 * @param uiState The current state of the timetable UI.
 * @param onRetryClick Callback invoked when the retry button is clicked.
 * @param topBar Composable for the top bar of the screen.
 * @param bottomBar Composable for the bottom bar of the screen (optional).
 * @param onItemVisibilityChange Callback invoked when the visibility of a timetable item changes.
 * @param selectedEventId Optional event ID to scroll to; once the scroll finishes, the
 * corresponding item plays a shake animation to draw attention to it. When absent, the list jumps
 * once, without animation, to the current day.
 */
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
    var eventIdToAnimate by remember { mutableStateOf<String?>(null) }
    var hasScrolledToCurrentDay by rememberSaveable { mutableStateOf(false) }
    val minuteTick = rememberMinuteTick()
    val ongoingEventIds = remember(uiState, minuteTick) { uiState.ongoingEventIds }
    val upcomingEventIds = remember(uiState, minuteTick) { uiState.upcomingEventIds }
    val currentDay = remember(uiState, minuteTick) { uiState.currentDay }

    LaunchedEffect(selectedEventId, uiState.timetableListItems, uiState.isLoading) {
        eventIdToAnimate = null
        val isListReady = !uiState.isLoading && uiState.timetableListItems.isNotEmpty()

        when {
            selectedEventId == null && !hasScrolledToCurrentDay && isListReady -> {
                hasScrolledToCurrentDay = true
                listState.animateScrollToItem(uiState.currentDayIndex)
            }

            else -> {
                val targetIndex = uiState.timetableListItems.indexOfFirst {
                    it is TimetableListItem.Event && it.id == selectedEventId
                }.takeIf { it >= FIRST_INDEX } ?: return@LaunchedEffect

                delay(SCROLL_START_DELAY)
                listState.animateScrollToItem(
                    index = (targetIndex - SCROLL_INDEX_OFFSET).coerceAtLeast(FIRST_INDEX)
                )

                eventIdToAnimate = selectedEventId
            }
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
            ongoingEventIds = ongoingEventIds,
            upcomingEventIds = upcomingEventIds,
            currentDay = currentDay,
            listState = listState,
            onVisibleClick = onItemVisibilityChange,
            onNotificationClick = onItemNotificationChange,
            onRemoveClick = onRemoveItem,
            onAddItem = onAddItem,
            selectedEventId = eventIdToAnimate,
        )
    }
}

/**
 * A counter that increases at the start of every minute, used to refresh time-based state.
 */
@Composable
private fun rememberMinuteTick(): Int {
    var tick by remember { mutableIntStateOf(FIRST_INDEX) }

    LaunchedEffect(Unit) {
        while (true) {
            val currentSecond = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .second
            delay((SECONDS_PER_MINUTE - currentSecond).seconds)
            tick++
        }
    }

    return tick
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