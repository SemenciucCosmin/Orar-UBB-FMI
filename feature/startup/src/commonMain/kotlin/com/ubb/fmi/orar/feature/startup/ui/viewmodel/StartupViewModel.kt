package com.ubb.fmi.orar.feature.startup.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.permissions.model.Permission
import com.ubb.fmi.orar.domain.notifications.usecase.InitializeTimetableNotificationsUseCase
import com.ubb.fmi.orar.domain.permissions.usecase.IsPermissionGrantedUseCase
import com.ubb.fmi.orar.domain.permissions.usecase.RequestPermissionUseCase
import com.ubb.fmi.orar.domain.timetable.usecase.CheckCachedNewsDataValidityUseCase
import com.ubb.fmi.orar.domain.timetable.usecase.CheckCachedTimetableDataValidityUseCase
import com.ubb.fmi.orar.domain.usertimetable.usecase.IsConfigurationDoneUseCase
import com.ubb.fmi.orar.feature.startup.ui.viewmodel.model.StartupUiEvent
import com.ubb.fmi.orar.ui.catalog.viewmodel.EventViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for handling the startup logic of the application.
 * It checks the configuration and cached data validity to determine the next steps.
 */
class StartupViewModel(
    private val checkCachedTimetableDataValidityUseCase: CheckCachedTimetableDataValidityUseCase,
    private val checkCachedNewsDataValidityUseCase: CheckCachedNewsDataValidityUseCase,
    private val isConfigurationDoneUseCase: IsConfigurationDoneUseCase,
    private val requestPermissionUseCase: RequestPermissionUseCase,
    private val isPermissionGrantedUseCase: IsPermissionGrantedUseCase,
    private val initializeTimetableNotificationsUseCase: InitializeTimetableNotificationsUseCase
) : EventViewModel<StartupUiEvent>() {

    /**
     * Initializes the ViewModel and checks the configuration validity.
     * This is called when the ViewModel is created.
     */
    init {
        checkDataValidity()
        checkConfiguration()
        checkNotificationPermission()
    }

    /**
     * Starts coroutines independent from ViewModel for checking cached data validity
     */
    private fun checkDataValidity() {
        viewModelScope.launch { checkCachedTimetableDataValidityUseCase() }
        viewModelScope.launch { checkCachedNewsDataValidityUseCase() }
    }

    /**
     * Requests notification permission from the user, if needed, so scheduled event
     * notifications can actually be displayed on both Android and iOS.
     */
    private fun checkNotificationPermission() {
        viewModelScope.launch {
            if (!isPermissionGrantedUseCase(Permission.NOTIFICATIONS)) {
                val isRequestGranted = requestPermissionUseCase(Permission.NOTIFICATIONS)
                if (isRequestGranted) initializeTimetableNotificationsUseCase()
            }
        }
    }

    /**
     * Checks the current configuration and cached data validity.
     * Depending on the results, it emits appropriate events to indicate
     * whether the configuration is complete or incomplete.
     */
    private fun checkConfiguration() {
        viewModelScope.launch {
            val isConfigurationDone = isConfigurationDoneUseCase().firstOrNull() == true
            when {
                isConfigurationDone -> registerEvent(StartupUiEvent.CONFIGURATION_COMPLETE)
                else -> registerEvent(StartupUiEvent.CONFIGURATION_INCOMPLETE)
            }
        }
    }
}
