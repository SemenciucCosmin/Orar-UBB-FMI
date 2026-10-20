package com.ubb.fmi.orar.feature.freerooms.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.rooms.repository.RoomsRepository
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.Week
import com.ubb.fmi.orar.domain.analytics.AnalyticsLogger
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent
import com.ubb.fmi.orar.domain.usertimetable.usecase.GetCurrentWeekUseCase
import com.ubb.fmi.orar.feature.freerooms.ui.viewmodel.model.FreeRoomsUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

/**
 * ViewModel for managing the state of the Free Rooms Search feature.
 */
class FreeRoomsViewModel(
    analyticsLogger: AnalyticsLogger,
    private val roomsRepository: RoomsRepository,
    private val getCurrentWeekUseCase: GetCurrentWeekUseCase,
    private val logger: Logger,
) : ViewModel() {

    /**
     * Mutable state flow representing the UI state of the Free Rooms Search feature.
     * It holds the list of rooms, loading state, and error state.
     */
    private val _uiState = MutableStateFlow(
        FreeRoomsUiState(
            isLoading = true,
            selectedDays = persistentListOf(today())
        )
    )
    val uiState = _uiState.asStateFlow()

    /**
     * Week type of the current calendar week, null until it is known.
     */
    private var currentFrequency: Frequency? = null

    /**
     * Initializes the ViewModel and starts fetching rooms.
     * The initial state is set to loading.
     */
    init {
        analyticsLogger.logEvent(AnalyticsEvent.FREE_ROOM_SEARCH)
        getItems()
        getCurrentWeek()
    }

    /**
     * Selects the current week once it is known.
     */
    private fun getCurrentWeek() {
        viewModelScope.launch {
            val frequency = when (getCurrentWeekUseCase().first()) {
                Week.ODD -> Frequency.WEEK_1
                Week.EVEN -> Frequency.WEEK_2
            }

            logger.d(TAG, "getCurrentWeek: $frequency")
            currentFrequency = frequency
            _uiState.update { it.copy(selectedFrequency = frequency) }
        }
    }

    /**
     * Fetches the list of rooms and events from the data source and updates the UI state.
     */
    private fun getItems() {
        viewModelScope.launch {
            logger.d(TAG, "getItems")
            roomsRepository.getAllRoomsAndEvents().collectLatest { (rooms, events) ->
                logger.d(TAG, "getItems rooms: ${rooms.size}")
                logger.d(TAG, "getItems events: ${events.size}")

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        rooms = rooms,
                        events = events
                    )
                }
            }
        }
    }

    fun changeStartHour(startHour: Int) {
        _uiState.update { it.copy(startHour = startHour) }
    }

    fun changeStartMinute(startMinute: Int) {
        _uiState.update { it.copy(startMinute = startMinute) }
    }

    fun changeEndHour(endHour: Int) {
        _uiState.update { it.copy(endHour = endHour) }
    }

    fun changeEndMinute(endMinute: Int) {
        _uiState.update { it.copy(endMinute = endMinute) }
    }

    fun selectDays(days: List<Day>) {
        _uiState.update { it.copy(selectedDays = days.toImmutableList()) }
    }

    fun selectFrequency(frequency: Frequency) {
        _uiState.update { it.copy(selectedFrequency = frequency) }
    }

    /**
     * Selects today and the current week, from now until an hour later, capped at the end of the
     * day.
     */
    fun selectNow() {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
        val endSecond = (now.toSecondOfDay() + SECONDS_IN_HOUR).coerceAtMost(LAST_SECOND_OF_DAY)
        val end = LocalTime.fromSecondOfDay(endSecond)

        _uiState.update {
            it.copy(
                selectedDays = persistentListOf(today()),
                selectedFrequency = currentFrequency ?: it.selectedFrequency,
                startHour = now.hour,
                startMinute = now.minute,
                endHour = end.hour,
                endMinute = end.minute,
            )
        }
    }

    private fun today(): Day {
        val dayOfWeekIndex = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .dayOfWeek
            .ordinal

        return Day.entries.first { it.orderIndex == dayOfWeekIndex }
    }

    companion object {
        private const val TAG = "FreeRoomsViewModel"
        private const val SECONDS_IN_HOUR = 3600
        private const val LAST_SECOND_OF_DAY = 86_340
    }
}
