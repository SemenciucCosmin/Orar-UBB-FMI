package com.ubb.fmi.orar.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.domain.analytics.AnalyticsLogger
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsParameter
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem
import com.ubb.fmi.orar.domain.colorpack.model.ColorPack
import com.ubb.fmi.orar.domain.colorpack.usecase.GetColorPackUseCase
import com.ubb.fmi.orar.domain.colorpack.usecase.ResetItemColorUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * ViewModel for the customizable colors list.
 * Exposes the current [ColorPack] and resets single items to their default color.
 */
class ColorsViewModel(
    getColorPackUseCase: GetColorPackUseCase,
    private val resetItemColorUseCase: ResetItemColorUseCase,
    private val analyticsLogger: AnalyticsLogger,
) : ViewModel() {

    val colorPack: StateFlow<ColorPack> = getColorPackUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
        initialValue = ColorPack.Default
    )

    fun resetColor(item: ColorItem) {
        analyticsLogger.logEvent(
            event = AnalyticsEvent.COLOR_RESET,
            params = mapOf(AnalyticsParameter.COLOR_ITEM to item.id),
        )

        viewModelScope.launch { resetItemColorUseCase(item) }
    }
}
