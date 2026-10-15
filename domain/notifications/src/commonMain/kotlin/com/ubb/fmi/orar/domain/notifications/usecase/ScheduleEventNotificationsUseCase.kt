package com.ubb.fmi.orar.domain.notifications.usecase

import Logger
import com.ubb.fmi.orar.data.notifications.model.EventNotification
import com.ubb.fmi.orar.data.notifications.repository.NotificationRepository
import com.ubb.fmi.orar.data.settings.preferences.SettingsPreferences
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.calendar.usecase.GetAcademicCalendarUseCase
import kotlinx.coroutines.flow.firstOrNull
import kotlin.time.Clock

/**
 * Maps each of [events] to an [EventNotification] and schedules it via [NotificationRepository],
 * using the user's configured [SettingsPreferences.getNotificationAdvanceMinutes] lead time and
 * the official academic calendar. Personal events aren't bound
 * to the teaching calendar and are scheduled all year round.
 *
 * This is the single choke point every scheduling path goes through, so it is also where the
 * global [SettingsPreferences.isNotificationsEnabled] switch is enforced: while notifications are
 * globally off nothing is scheduled, no matter which caller asks. Callers remain responsible for
 * the per-event decision (e.g. only events that are visible and have notifications enabled).
 */
class ScheduleEventNotificationsUseCase(
    private val notificationRepository: NotificationRepository,
    private val settingsPreferences: SettingsPreferences,
    private val timetablePreferences: TimetablePreferences,
    private val getAcademicCalendarUseCase: GetAcademicCalendarUseCase,
    private val logger: Logger,
) {
    suspend operator fun invoke(vararg events: Event) {
        if (events.isEmpty()) return

        if (!settingsPreferences.isNotificationsEnabled()) {
            logger.d(TAG, "Skipped scheduling ${events.size} event(s), notifications are globally off")
            return
        }

        val advanceMinutes = settingsPreferences.getNotificationAdvanceMinutes()
        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        val currentMillis = Clock.System.now().toEpochMilliseconds()

        val academicCalendar = getAcademicCalendarUseCase().firstOrNull()

        // Events are cached per year *and* semester, so they may only be notified about during
        // the semester they were fetched for. Scheduling them across the whole academic year
        // would keep last semester's timetable firing against this semester's schedule.
        val configuredSemester = academicCalendar?.semesters?.firstOrNull { academicSemester ->
            academicSemester.semester.id == configuration?.semesterId
        }

        val academicSemester = configuredSemester ?: academicCalendar?.semesters?.firstOrNull {
            currentMillis in it
        }

        val teachingWeeks = academicSemester?.teachingWeeks
        if (configuredSemester == null) {
            logger.d(
                TAG,
                "Configured semester ${configuration?.semesterId} not resolved for year " +
                    "${academicCalendar?.startYear}, fell back to semester in session: " +
                    "${academicSemester?.semester?.id}",
            )
        }

        logger.d(
            TAG,
            "Scheduling context: advanceMinutes=$advanceMinutes, configuredYear=${configuration?.year}, " +
                "academicYear=${academicCalendar?.startYear}, semester=${academicSemester?.semester?.id}, " +
                "isFinal=${academicSemester?.isFinal}, teachingWeeks=${teachingWeeks?.size}",
        )

        val (personalEvents, timetableEvents) = events.partition { it.type == EventType.PERSONAL }
        val schedulableEvents = when (teachingWeeks) {
            null -> {
                logger.d(TAG, "Skipped ${timetableEvents.size} timetable event(s), no semester is in session")
                personalEvents
            }

            else -> events.toList()
        }

        val eventNotifications = schedulableEvents.map { event ->
            EventNotification(
                id = event.id,
                activity = event.activity,
                type = event.type,
                location = event.location,
                participant = event.participant,
                frequency = event.frequency,
                day = event.day,
                startHour = event.startHour,
                startMinute = event.startMinute,
                endHour = event.endHour,
                endMinute = event.endMinute,
                advanceMinutes = advanceMinutes,
                teachingWeeks = teachingWeeks.takeIf { event.type != EventType.PERSONAL },
            )
        }

        logger.d(
            TAG,
            "Scheduling ${eventNotifications.size} event notification(s): " +
                eventNotifications.joinToString { it.id },
        )
        notificationRepository.schedule(eventNotifications)
    }

    companion object {
        private const val TAG = "ScheduleEventNotificationsUseCase"
    }
}
