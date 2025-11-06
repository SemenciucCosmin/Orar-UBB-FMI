package com.ubb.fmi.orar.app

import com.ubb.fmi.orar.domain.feedback.usecase.IncreaseAppUsagePointsUseCase
import com.ubb.fmi.orar.domain.feedback.usecase.SetFirstUsageTimestampUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

/**
 * Initializer class for all core shared processes.
 */
class AppInitializer : KoinComponent {

    private val coroutineScope: CoroutineScope by inject()

    private val setFirstUsageTimestampUseCase: SetFirstUsageTimestampUseCase by inject()

    private val increaseAppUsagePointsUseCase: IncreaseAppUsagePointsUseCase by inject()

    fun initApp() {
        coroutineScope.launch { setFirstUsageTimestampUseCase() }
        coroutineScope.launch { increaseAppUsagePointsUseCase() }
    }
}