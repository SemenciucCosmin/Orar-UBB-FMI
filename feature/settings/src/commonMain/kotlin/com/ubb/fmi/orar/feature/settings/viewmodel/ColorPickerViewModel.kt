package com.ubb.fmi.orar.feature.settings.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.domain.analytics.AnalyticsLogger
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsParameter
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem
import com.ubb.fmi.orar.domain.colorpack.usecase.GetColorPackUseCase
import com.ubb.fmi.orar.domain.colorpack.usecase.SetItemColorUseCase
import com.ubb.fmi.orar.feature.settings.viewmodel.model.ColorPickerUiState
import com.ubb.fmi.orar.feature.settings.viewmodel.model.ColorPickerUiState.ColorPickerUiEvent
import com.ubb.fmi.orar.ui.catalog.viewmodel.EventViewModel
import com.ubb.fmi.orar.ui.colorpack.extensions.getColor
import com.ubb.fmi.orar.ui.colorpack.extensions.toArgbLong
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for picking the color of a single [ColorItem].
 * Keeps an unsaved selection that is persisted only on [save].
 */
class ColorPickerViewModel(
    colorItemId: String,
    private val getColorPackUseCase: GetColorPackUseCase,
    private val setItemColorUseCase: SetItemColorUseCase,
    private val analyticsLogger: AnalyticsLogger,
) : EventViewModel<ColorPickerUiEvent>() {

    private val item = ColorItem.getById(colorItemId)

    private val _uiState = MutableStateFlow(
        ColorPickerUiState(item = item ?: ColorItem.entries.first())
    )
    val uiState = _uiState.asStateFlow()

    init {
        when (item) {
            null -> registerEvent(ColorPickerUiEvent.INVALID_ITEM)
            else -> viewModelScope.launch {
                val colorPack = getColorPackUseCase().first()
                _uiState.update {
                    it.copy(colorPack = colorPack, selectedColor = colorPack.getColor(item))
                }
            }
        }
    }

    fun selectColor(color: Color) {
        _uiState.update { it.copy(selectedColor = color) }
    }

    fun save() {
        val state = _uiState.value

        analyticsLogger.logEvent(
            event = AnalyticsEvent.COLOR_CHANGED,
            params = mapOf(AnalyticsParameter.COLOR_ITEM to state.item.id),
        )

        viewModelScope.launch {
            setItemColorUseCase(state.item, state.selectedColor.toArgbLong())
            registerEvent(ColorPickerUiEvent.SAVED)
        }
    }
}
