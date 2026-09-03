package com.ubb.fmi.orar.app

import com.ubb.fmi.orar.domain.feedback.usecase.IncreaseAppUsagePointsUseCase
import com.ubb.fmi.orar.domain.feedback.usecase.SetFirstUsageTimestampUseCase
import com.ubb.fmi.orar.domain.notifications.usecase.RescheduleCachedNotificationsUseCase
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

    private val rescheduleCachedNotificationsUseCase: RescheduleCachedNotificationsUseCase by inject()

    fun initApp() {
        coroutineScope.launch { setFirstUsageTimestampUseCase() }
        coroutineScope.launch { increaseAppUsagePointsUseCase() }

        // Refreshes notifications on every app start (not just on first permission grant, see
        // StartupViewModel), so alarms/pending notifications lost outside the app's control
        // (e.g. Android clearing AlarmManager alarms on reboot) get restored. Uses the
        // cache-only RescheduleCachedNotificationsUseCase rather than
        // InitializeTimetableNotificationsUseCase, which would force-enable notifications for
        // every visible event and silently override events the user turned notifications off for.
        coroutineScope.launch { rescheduleCachedNotificationsUseCase() }
    }
}
