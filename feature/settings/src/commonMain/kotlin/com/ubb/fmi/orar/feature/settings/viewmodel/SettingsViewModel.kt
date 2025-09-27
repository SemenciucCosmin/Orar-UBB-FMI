package com.ubb.fmi.orar.feature.settings.viewmodel

import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.domain.timetable.usecase.InvalidateCachedDataUseCase
import com.ubb.fmi.orar.feature.settings.viewmodel.model.SettingsEvent
import com.ubb.fmi.orar.ui.catalog.viewmodel.EventViewModel
import kotlinx.coroutines.launch

/**
 * View model for handling settings screen logic
 */
class SettingsViewModel(
    private val invalidateCachedDataUseCase: InvalidateCachedDataUseCase,
) : EventViewModel<SettingsEvent>() {

    /**
     * Forces data refresh by invalidating the cached data
     */
    fun refreshData() {
        viewModelScope.launch {
            invalidateCachedDataUseCase()
            registerEvent(SettingsEvent.SUCCESSFUL_REFRESH)
        }
    }
}
