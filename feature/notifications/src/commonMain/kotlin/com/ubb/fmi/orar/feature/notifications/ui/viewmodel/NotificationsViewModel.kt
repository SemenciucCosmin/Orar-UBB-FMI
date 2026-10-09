package com.ubb.fmi.orar.feature.notifications.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.settings.preferences.SettingsPreferences
import com.ubb.fmi.orar.domain.notifications.usecase.InitializeTimetableNotificationsUseCase
import com.ubb.fmi.orar.domain.notifications.usecase.InvalidateTimetableNotificationsUseCase
import com.ubb.fmi.orar.domain.notifications.usecase.RescheduleNotificationsUseCase
import com.ubb.fmi.orar.feature.notifications.ui.viewmodel.model.NotificationsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the notifications settings screen. Exposes the global notifications on/off
 * switch and its configurable advance lead time, and reacts to changes immediately.
 */
class NotificationsViewModel(
    private val settingsPreferences: SettingsPreferences,
    private val initializeTimetableNotificationsUseCase: InitializeTimetableNotificationsUseCase,
    private val invalidateTimetableNotificationsUseCase: InvalidateTimetableNotificationsUseCase,
    private val rescheduleNotificationsUseCase: RescheduleNotificationsUseCase,
    private val logger: Logger,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getNotificationsSettings()
    }

    private fun getNotificationsSettings() {
        viewModelScope.launch {
            combine(
                settingsPreferences.getNotificationsEnabledFlow(),
                settingsPreferences.getNotificationAdvanceMinutesFlow(),
            ) { notificationsEnabled, notificationAdvanceMinutes ->
                NotificationsUiState(
                    notificationsEnabled = notificationsEnabled,
                    notificationAdvanceMinutes = notificationAdvanceMinutes % MINUTES_IN_HOUR,
                    notificationAdvanceHours = notificationAdvanceMinutes / MINUTES_IN_HOUR,
                )
            }.collectLatest { uiState -> _uiState.update { uiState } }
        }
    }

    /**
     * Persists the global notifications switch and reacts immediately: turning it on re-syncs
     * and re-schedules every visible event, turning it off cancels everything scheduled.
     *
     * The preference write is awaited before scheduling runs, because scheduling now reads that
     * same flag to decide whether it is allowed to arm anything; kicking both off concurrently
     * would let the scheduler observe the previous value and no-op.
     *
     * This is the one place invalidation runs with `forceAll`, since muting is the only case
     * where personal event notifications must go quiet too. Their per-event preference survives,
     * so switching back on restores exactly what the user had.
     */
    fun setNotificationsEnabled(enabled: Boolean) {
        logger.d(TAG, "setNotificationsEnabled: $enabled")

        viewModelScope.launch {
            settingsPreferences.setNotificationsEnabled(enabled)

            when {
                enabled -> initializeTimetableNotificationsUseCase()
                else -> invalidateTimetableNotificationsUseCase(forceAll = true)
            }
        }
    }

    /**
     * Persists the notification advance lead time and reschedules every cached event that
     * currently has notifications on, so the new lead time takes effect immediately.
     */
    fun setNotificationAdvanceMinutes(advanceMinutes: Int) {
        logger.d(TAG, "setNotificationAdvanceMinutes: $advanceMinutes")
        viewModelScope.launch {
            settingsPreferences.setNotificationAdvanceMinutes(advanceMinutes)
            rescheduleNotificationsUseCase()
        }
    }

    companion object {
        private const val TAG = "NotificationsViewModel"
        private const val MINUTES_IN_HOUR = 60
    }
}
