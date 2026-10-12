package com.ubb.fmi.orar.feature.notifications.ui.viewmodel

import Logger
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.permissions.model.Permission
import com.ubb.fmi.orar.data.settings.preferences.SettingsPreferences
import com.ubb.fmi.orar.domain.analytics.AnalyticsLogger
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsParameter
import com.ubb.fmi.orar.domain.notifications.usecase.InitializeTimetableNotificationsUseCase
import com.ubb.fmi.orar.domain.notifications.usecase.InvalidateTimetableNotificationsUseCase
import com.ubb.fmi.orar.domain.notifications.usecase.RescheduleNotificationsUseCase
import com.ubb.fmi.orar.domain.permissions.usecase.IsPermissionGrantedUseCase
import com.ubb.fmi.orar.domain.permissions.usecase.OpenPermissionSettingsUseCase
import com.ubb.fmi.orar.feature.notifications.ui.viewmodel.model.NotificationsUiState
import com.ubb.fmi.orar.feature.notifications.ui.viewmodel.model.NotificationsUiState.NotificationsUiEvent
import com.ubb.fmi.orar.ui.catalog.viewmodel.EventViewModel
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
    private val isPermissionGrantedUseCase: IsPermissionGrantedUseCase,
    private val openPermissionSettingsUseCase: OpenPermissionSettingsUseCase,
    private val analyticsLogger: AnalyticsLogger,
    private val logger: Logger,
) : EventViewModel<NotificationsUiEvent>() {

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
                notificationsEnabled to notificationAdvanceMinutes
            }.collectLatest { (notificationsEnabled, notificationAdvanceMinutes) ->
                _uiState.update {
                    it.copy(
                        notificationsEnabled = notificationsEnabled,
                        notificationAdvanceMinutes = notificationAdvanceMinutes % MINUTES_IN_HOUR,
                        notificationAdvanceHours = notificationAdvanceMinutes / MINUTES_IN_HOUR,
                    )
                }
            }
        }
    }

    /**
     * Re-checks the notification permission, e.g. when returning from the system settings.
     * If it was just granted, restores the notifications the user already had enabled.
     */
    fun refreshNotificationPermission() {
        viewModelScope.launch {
            val isGranted = isPermissionGrantedUseCase(Permission.NOTIFICATIONS)
            val wasGranted = _uiState.value.isNotificationPermissionGranted
            logger.d(TAG, "refreshNotificationPermission, granted: $isGranted")
            _uiState.update { it.copy(isNotificationPermissionGranted = isGranted) }
            if (isGranted && !wasGranted) rescheduleNotificationsUseCase()
        }
    }

    /**
     * Opens the system settings screen where the notification permission can be granted.
     */
    fun openNotificationPermissionSettings() {
        openPermissionSettingsUseCase(Permission.NOTIFICATIONS)
    }

    /**
     * Prompts the user to grant the notification permission instead of changing a setting when
     * it isn't granted. Returns whether the permission is granted.
     */
    private fun ensureNotificationPermission(): Boolean {
        if (_uiState.value.isNotificationPermissionGranted) return true
        registerEvent(NotificationsUiEvent.NOTIFICATION_PERMISSION_REQUIRED) {
            it == NotificationsUiEvent.NOTIFICATION_PERMISSION_REQUIRED
        }
        return false
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
        if (!ensureNotificationPermission()) return
        logger.d(TAG, "setNotificationsEnabled: $enabled")
        analyticsLogger.logEvent(
            when {
                enabled -> AnalyticsEvent.NOTIFICATIONS_ENABLED
                else -> AnalyticsEvent.NOTIFICATIONS_DISABLED
            }
        )

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
        if (!ensureNotificationPermission()) return
        logger.d(TAG, "setNotificationAdvanceMinutes: $advanceMinutes")
        analyticsLogger.logEvent(
            event = AnalyticsEvent.NOTIFICATION_ADVANCE_CHANGED,
            params = mapOf(AnalyticsParameter.ADVANCE_MINUTES to advanceMinutes),
        )
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
