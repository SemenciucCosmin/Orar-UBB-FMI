package com.ubb.fmi.orar.domain.notifications.usecase

import Logger
import com.ubb.fmi.orar.data.notifications.repository.NotificationRepository
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource

/**
 * Restores notifications for events that were already marked as having notifications on,
 * using only cached data (no network calls). This is meant to be called from contexts where
 * a full [InitializeTimetableNotificationsUseCase] sync isn't appropriate or possible, such as
 * right after a device reboot, since Android clears every [android.app.AlarmManager] alarm on
 * reboot and iOS can lose pending notifications if the app hasn't refreshed them in a while.
 *
 * Every still-pending notification is cancelled before rescheduling, so changes that affect
 * the trigger time (e.g. a new notification advance lead time) never leave the previously
 * scheduled occurrences behind alongside the new ones.
 */
class RescheduleNotificationsUseCase(
    private val eventsDataSource: EventsDataSource,
    private val notificationRepository: NotificationRepository,
    private val scheduleEventNotificationsUseCase: ScheduleEventNotificationsUseCase,
    private val logger: Logger,
) {
    suspend operator fun invoke() {
        val eventsWithNotificationsOn = eventsDataSource.getEventsWithNotificationsOnFromCache()
        logger.d(TAG, "Rescheduling ${eventsWithNotificationsOn.size} cached event notification(s)")
        if (eventsWithNotificationsOn.isEmpty()) return

        eventsWithNotificationsOn.forEach { event ->
            notificationRepository.cancel(event.id)
        }

        scheduleEventNotificationsUseCase(*eventsWithNotificationsOn.toTypedArray())
    }

    companion object {
        private const val TAG = "RescheduleCachedNotificationsUseCase"
    }
}
