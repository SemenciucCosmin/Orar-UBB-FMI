package com.ubb.fmi.orar.domain.usertimetable.usecase

import Logger
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.analytics.AnalyticsLogger
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.notifications.usecase.ScheduleEventNotificationsUseCase
import kotlinx.coroutines.flow.firstOrNull
import okio.ByteString.Companion.encodeUtf8

class AddCustomEventsUseCase(
    private val timetablePreferences: TimetablePreferences,
    private val eventsDataSource: EventsDataSource,
    private val analyticsLogger: AnalyticsLogger,
    private val scheduleEventNotificationsUseCase: ScheduleEventNotificationsUseCase,
    private val logger: Logger,
) {
    suspend operator fun invoke(
        activity: String,
        location: String,
        caption: String,
        details: String,
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        frequency: Frequency,
        days: List<Day>,
    ) {
        val configuration = timetablePreferences.getConfiguration().firstOrNull() ?: return
        val configurationId = configuration.year.toString() + configuration.semesterId
        val customEvents = days.map { day ->
            val id = listOf(
                activity,
                location,
                caption,
                details,
                startHour,
                startMinute,
                endHour,
                endMinute,
                frequency.id,
                day.id
            ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

            Event(
                id = id,
                configurationId = configurationId,
                ownerId = Owner.User.id,
                day = day,
                frequency = frequency,
                startHour = startHour,
                startMinute = startMinute,
                endHour = endHour,
                endMinute = endMinute,
                location = location,
                activity = activity,
                type = EventType.CUSTOM,
                participant = String.BLANK,
                caption = caption,
                details = details,
                isVisible = true,
                isNotificationOn = true,
            )
        }

        analyticsLogger.logEvent(AnalyticsEvent.ADD_CUSTOM_EVENT)
        eventsDataSource.saveEventsInCache(
            ownerId = Owner.User.id,
            events = customEvents
        )

        // The events are created with notifications already on, so they have to be scheduled
        // right away; otherwise the user would get no reminder for a freshly added custom
        // event until the app is next restarted and cached notifications are rescheduled.
        logger.d(
            TAG,
            "Scheduling notifications for ${customEvents.size} new custom event(s): " +
                customEvents.joinToString { it.id }
        )
        scheduleEventNotificationsUseCase(*customEvents.toTypedArray())
    }

    companion object {
        private const val TAG = "AddCustomEventsUseCase"
    }
}
