package com.ubb.fmi.orar.domain.usertimetable.usecase

import Logger
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.domain.analytics.AnalyticsLogger
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.notifications.usecase.ScheduleEventNotificationsUseCase
import okio.ByteString.Companion.encodeUtf8

/**
 * Creates a copy in database of an event under the [Owner.User]
 * This allows the user to introduce foreign events into its timetable
 */
class AdoptEventUseCase(
    private val eventsDataSource: EventsDataSource,
    private val analyticsLogger: AnalyticsLogger,
    private val scheduleEventNotificationsUseCase: ScheduleEventNotificationsUseCase,
    private val logger: Logger,
) {
    suspend operator fun invoke(eventId: String) {
        val event = eventsDataSource.getEventFromCache(eventId) ?: run {
            logger.e(TAG, "Adopt aborted, event $eventId is missing from cache")
            return
        }
        val id = listOf(
            event.id,
            Owner.User.id
        ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

        val adoptedEvent = event.copy(id = id, isNotificationOn = true)
        analyticsLogger.logEvent(AnalyticsEvent.ADOPT_EVENT)
        eventsDataSource.saveEventInCache(Owner.User.id, adoptedEvent)
        logger.d(TAG, "Adopted event $eventId as $id, scheduling its notification")
        scheduleEventNotificationsUseCase(adoptedEvent)
    }

    companion object {
        private const val TAG = "AdoptEventUseCase"
    }
}
