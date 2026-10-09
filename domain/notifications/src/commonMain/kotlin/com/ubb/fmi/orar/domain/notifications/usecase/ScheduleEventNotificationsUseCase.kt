package com.ubb.fmi.orar.domain.notifications.usecase

import Logger
import com.ubb.fmi.orar.data.notifications.model.EventNotification
import com.ubb.fmi.orar.data.notifications.repository.NotificationRepository
import com.ubb.fmi.orar.data.settings.preferences.SettingsPreferences
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.calendar.usecase.GetAcademicYearUseCase
import kotlinx.coroutines.flow.firstOrNull
import kotlin.time.Clock

/**
 * Maps each of [events] to an [EventNotification] and schedules it via [NotificationRepository],
 * using the user's configured [SettingsPreferences.getNotificationAdvanceMinutes] lead time and
 * the teaching calendar of the currently configured academic year. Personal events aren't bound
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
    private val getAcademicYearUseCase: GetAcademicYearUseCase,
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

        // Falling back to the academic year the current date sits in, rather than to its
        // calendar year, keeps January-to-September correct: those months belong to the
        // academic year that started the previous autumn.
        val academicYear = when (val year = configuration?.year) {
            null -> getAcademicYearUseCase(currentMillis)
            else -> getAcademicYearUseCase(year)
        }

        // Events are cached per year *and* semester, so they may only be notified about during
        // the semester they were fetched for. Scheduling them across the whole academic year
        // would keep last semester's timetable firing against this semester's schedule.
        val semesterNumber = configuration?.semesterId?.toIntOrNull()
        val configuredSemester = semesterNumber?.let { number ->
            academicYear.semesters.firstOrNull { it.index == number }
        }
        val academicSemester = configuredSemester ?: academicYear.getSemesterAt(currentMillis)

        if (configuredSemester == null) {
            logger.d(
                TAG,
                "Configured semester ${configuration?.semesterId} not resolved for year " +
                    "${academicYear.startYear}, fell back to semester in session: ${academicSemester?.index}",
            )
        }

        logger.d(
            TAG,
            "Scheduling context: advanceMinutes=$advanceMinutes, configuredYear=${configuration?.year}, " +
                "academicYear=${academicYear.startYear}, semester=${academicSemester?.index} " +
                "[${academicSemester?.startMillis}..${academicSemester?.endMillis}], " +
                "teachingWeeks=${academicSemester?.teachingWeeks?.size}",
        )

        val (personalEvents, timetableEvents) = events.partition { it.type == EventType.PERSONAL }
        val schedulableEvents = when (academicSemester) {
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
                academicSemester = academicSemester.takeIf { event.type != EventType.PERSONAL },
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
