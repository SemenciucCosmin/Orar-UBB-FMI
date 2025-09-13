package com.ubb.fmi.orar.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.settings.preferences.SettingsPreferences
import com.ubb.fmi.orar.domain.theme.model.ThemeOption
import com.ubb.fmi.orar.domain.theme.usecase.GetThemeOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * ViewModel for theme settings selection
 * Fetches selected theme option from preferences
 * Saves a new theme option to preferences
 */
class ThemeViewModel(
    private val settingsPreferences: SettingsPreferences,
    private val getThemeOption: GetThemeOption,
) : ViewModel() {

    private val _themeOption = MutableStateFlow(ThemeOption.SYSTEM)
    val themeOption = _themeOption.asStateFlow()
        .onStart {
            viewModelScope.launch {
                getThemeOption().collectLatest { themeOption ->
                    _themeOption.update { themeOption }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _themeOption.value
        )

    fun selectThemeOption(themeOption: ThemeOption) {
        viewModelScope.launch { settingsPreferences.setThemeOption(themeOption.id) }
    }
}
