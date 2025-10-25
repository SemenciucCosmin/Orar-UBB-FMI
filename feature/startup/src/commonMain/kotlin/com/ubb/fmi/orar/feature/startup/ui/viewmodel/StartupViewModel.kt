package com.ubb.fmi.orar.feature.startup.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.domain.timetable.usecase.CheckCachedNewsDataValidityUseCase
import com.ubb.fmi.orar.domain.timetable.usecase.CheckCachedTimetableDataValidityUseCase
import com.ubb.fmi.orar.domain.usertimetable.usecase.IsConfigurationDoneUseCase
import com.ubb.fmi.orar.feature.startup.ui.viewmodel.model.StartupUiEvent
import com.ubb.fmi.orar.ui.catalog.viewmodel.EventViewModel
import kotlinx.coroutines.CoroutineScope
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
    private val coroutineScope: CoroutineScope,
) : EventViewModel<StartupUiEvent>() {

    /**
     * Initializes the ViewModel and checks the configuration validity.
     * This is called when the ViewModel is created.
     */
    init {
        checkDataValidity()
        checkConfiguration()
    }

    /**
     * Starts coroutines independent from ViewModel for checking cached data validity
     */
    private fun checkDataValidity() {
        coroutineScope.launch { checkCachedTimetableDataValidityUseCase() }
        coroutineScope.launch { checkCachedNewsDataValidityUseCase() }
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
