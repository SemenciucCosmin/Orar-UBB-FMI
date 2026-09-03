package com.ubb.fmi.orar.data.notifications.repository

import Logger
import com.ubb.fmi.orar.data.notifications.datasource.NotificationCacheDataSource
import com.ubb.fmi.orar.data.notifications.model.EventNotification
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.domain.extensions.formatTime
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarIdentifierGregorian
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitWeekOfYear
import platform.Foundation.NSCalendarUnitWeekday
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import kotlin.coroutines.resume

@Suppress("TooManyFunctions")
class NotificationRepositoryImpl(
    private val notificationCacheDataSource: NotificationCacheDataSource,
    private val logger: Logger,
) : NotificationRepository {

    override suspend fun schedule(notification: EventNotification) {
        requestAuthorizationIfNeeded()
        val content = buildNotificationContent(notification)

        when (notification.frequency) {
            Frequency.BOTH -> scheduleWeeklyNotification(notification, content)
            else -> scheduleBiWeeklyNotification(notification, content)
        }
    }

    private fun buildNotificationContent(notification: EventNotification): UNMutableNotificationContent {
        val startTimeLabel = formatTime(notification.startHour, notification.startMinute)
        val endTimeLabel = formatTime(notification.endHour, notification.endMinute)

        return UNMutableNotificationContent().apply {
            setTitle("${notification.activity} • $startTimeLabel - $endTimeLabel")
            setBody("${notification.type.id} • ${notification.participant} • ${notification.location}")
        }
    }

    private fun scheduleWeeklyNotification(
        notification: EventNotification,
        content: UNMutableNotificationContent,
    ) {
        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = notification.id,
            content = content,
            trigger = buildWeeklyTrigger(notification),
        )

        addNotificationRequest(request)
    }

    private suspend fun scheduleBiWeeklyNotification(
        notification: EventNotification,
        content: UNMutableNotificationContent,
    ) {
        // Clear any previously scheduled occurrences for this event first so a
        // reduced budget/occurrence count never leaves stale orphaned requests
        // behind, wasting the app-wide 64 pending notification slots iOS allows.
        notificationCacheDataSource
            .getNotificationCenter()
            .removePendingNotificationRequestsWithIdentifiers(allNotificationIdentifiers(notification.id))

        val firstOccurrenceSeconds = secondsUntilFirstBiWeeklyOccurrence(notification)
        val occurrenceCount = resolveAvailableOccurrenceCount(notification.id)

        if (occurrenceCount <= 0) {
            logger.e(TAG, "Skipped scheduling notification ${notification.id}: pending notification budget exhausted")
            return
        }

        repeat(occurrenceCount) { occurrenceIndex ->
            val occurrenceOffsetSeconds = occurrenceIndex * BI_WEEKLY_INTERVAL_DAYS * DAY_IN_SECONDS
            val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(
                timeInterval = firstOccurrenceSeconds + occurrenceOffsetSeconds,
                repeats = false,
            )

            val request = UNNotificationRequest.requestWithIdentifier(
                identifier = biWeeklyIdentifier(notification.id, occurrenceIndex),
                content = content,
                trigger = trigger,
            )

            addNotificationRequest(request)
        }
    }

    /**
     * iOS caps every app at 64 pending local notifications
     * system-wide; requests beyond that are silently dropped with no error surfaced to the
     * app unless the completion handler is inspected. This keeps a safety margin below that
     * hard limit and trims how many future occurrences we ask for accordingly.
     */
    private suspend fun resolveAvailableOccurrenceCount(id: String): Int {
        val pendingCount = getPendingNotificationsCount()
        val availableBudget = (MAX_PENDING_NOTIFICATIONS_BUDGET - pendingCount).coerceAtLeast(0)

        if (availableBudget < BI_WEEKLY_OCCURRENCE_COUNT) {
            logger.i(
                TAG,
                "Limited notification $id to $availableBudget occurrences " +
                    "(pending=$pendingCount, budget=$MAX_PENDING_NOTIFICATIONS_BUDGET)",
            )
        }

        return minOf(BI_WEEKLY_OCCURRENCE_COUNT, availableBudget)
    }

    private suspend fun getPendingNotificationsCount(): Int = suspendCancellableCoroutine { cont ->
        notificationCacheDataSource.getNotificationCenter().getPendingNotificationRequestsWithCompletionHandler {
            cont.resume(it?.size ?: 0)
        }
    }

    private fun addNotificationRequest(request: UNNotificationRequest) {
        notificationCacheDataSource.getNotificationCenter().addNotificationRequest(
            request = request,
        ) { error ->
            // The handler was previously null, silently swallowing scheduling failures
            // (e.g. exceeding the pending notification limit). Logging here at least
            // makes the loss traceable instead of the notification vanishing unnoticed.
            if (error != null) {
                logger.e(TAG, "Failed to schedule notification ${request.identifier}: ${error.localizedDescription}")
            }
        }
    }

    override suspend fun cancel(id: String) {
        notificationCacheDataSource
            .getNotificationCenter()
            .removePendingNotificationRequestsWithIdentifiers(allNotificationIdentifiers(id))
    }

    private suspend fun requestAuthorizationIfNeeded() {
        val requestedPermissions = UNAuthorizationOptionAlert or
            UNAuthorizationOptionSound or
            UNAuthorizationOptionBadge

        suspendCancellableCoroutine { cont ->
            notificationCacheDataSource.getNotificationCenter().requestAuthorizationWithOptions(
                options = requestedPermissions,
            ) { _, _ -> cont.resume(Unit) }
        }
    }

    private fun buildWeeklyTrigger(notification: EventNotification): UNCalendarNotificationTrigger {
        val components = NSDateComponents().apply {
            weekday = notification.day.toIosWeekday().toLong()
            hour = notification.startHour.toLong()
            minute = notification.startMinute.toLong()
            second = 0
        }

        return UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
            dateComponents = components,
            repeats = true,
        )
    }

    private fun biWeeklyIdentifier(id: String, occurrenceIndex: Int) = "${id}_bw_$occurrenceIndex"

    private fun allNotificationIdentifiers(id: String): List<String> =
        listOf(id) + List(BI_WEEKLY_OCCURRENCE_COUNT) { occurrenceIndex -> biWeeklyIdentifier(id, occurrenceIndex) }

    private fun secondsUntilFirstBiWeeklyOccurrence(notification: EventNotification): Double {
        val now = NSDate()
        val calendar = NSCalendar(calendarIdentifier = NSCalendarIdentifierGregorian)
        val currentComponents = calendar.components(
            fromDate = now,
            unitFlags = NSCalendarUnitWeekday or
                NSCalendarUnitWeekOfYear or
                NSCalendarUnitHour or
                NSCalendarUnitMinute,
        )

        val currentWeekday = currentComponents.weekday.toInt()
        val currentHour = currentComponents.hour.toInt()
        val currentMinute = currentComponents.minute.toInt()
        val targetWeekday = notification.day.toIosWeekday()

        val daysUntilTargetWeekday = daysUntilNextWeekdayOccurrence(
            targetWeekday = targetWeekday,
            currentWeekday = currentWeekday,
            eventAlreadyStartedToday = isEventStartTimeReached(
                notification = notification,
                hour = currentHour,
                minute = currentMinute
            ),
        )

        val secondsUntilTargetDay = daysUntilTargetWeekday * DAY_IN_SECONDS
        val estimatedTargetDate = NSDate(now.timeIntervalSinceReferenceDate + secondsUntilTargetDay)
        val estimatedTargetWeekOfYear = calendar
            .components(NSCalendarUnitWeekOfYear, estimatedTargetDate)
            .weekOfYear
            .toInt()

        val targetWeekIsOdd = estimatedTargetWeekOfYear % 2 != 0
        val notificationRequiresOddWeek = notification.frequency == Frequency.WEEK_1
        val weekParityMismatches = targetWeekIsOdd != notificationRequiresOddWeek
        val weekParityAdjustmentSeconds = if (weekParityMismatches) DAY_IN_SECONDS * DAYS_IN_WEEK else 0.0

        val targetStartHourSeconds = notification.startHour * SECONDS_IN_HOUR
        val targetStartMinuteSeconds = notification.startMinute * SECONDS_IN_MINUTE
        val targetTimeOfDaySeconds = targetStartHourSeconds + targetStartMinuteSeconds

        val currentHourSeconds = currentHour * SECONDS_IN_HOUR
        val currentMinuteSeconds = currentMinute * SECONDS_IN_MINUTE
        val currentTimeOfDaySeconds = currentHourSeconds + currentMinuteSeconds

        val adjustedSecondsUntilTargetDay = secondsUntilTargetDay + weekParityAdjustmentSeconds
        val timeOfDayDifferenceSeconds = targetTimeOfDaySeconds - currentTimeOfDaySeconds
        return adjustedSecondsUntilTargetDay + timeOfDayDifferenceSeconds
    }

    private fun daysUntilNextWeekdayOccurrence(
        targetWeekday: Int,
        currentWeekday: Int,
        eventAlreadyStartedToday: Boolean,
    ): Int {
        val weekdayOffset = targetWeekday - currentWeekday
        val weekdayOffsetWithoutNegatives = weekdayOffset + DAYS_IN_WEEK
        val daysUntilSameWeekdayOccurrence = weekdayOffsetWithoutNegatives % DAYS_IN_WEEK
        val targetWeekdayIsToday = daysUntilSameWeekdayOccurrence == 0

        return when {
            targetWeekdayIsToday && eventAlreadyStartedToday -> DAYS_IN_WEEK
            else -> daysUntilSameWeekdayOccurrence
        }
    }

    private fun isEventStartTimeReached(notification: EventNotification, hour: Int, minute: Int): Boolean {
        val startHourAlreadyPassed = notification.startHour < hour
        val startHourIsNow = notification.startHour == hour
        val startMinuteAlreadyReached = notification.startMinute <= minute
        val startHourIsNowAndMinuteReached = startHourIsNow && startMinuteAlreadyReached
        return startHourAlreadyPassed || startHourIsNowAndMinuteReached
    }

    @Suppress("MagicNumber")
    private fun Day.toIosWeekday(): Int = when (this) {
        Day.SUNDAY -> 1
        Day.MONDAY -> 2
        Day.TUESDAY -> 3
        Day.WEDNESDAY -> 4
        Day.THURSDAY -> 5
        Day.FRIDAY -> 6
        Day.SATURDAY -> 7
    }

    companion object {
        private const val TAG = "NotificationRepository"
        private const val MINUTES_IN_HOUR = 60
        private const val SECONDS_IN_MINUTE = 60.0
        private const val SECONDS_IN_HOUR = MINUTES_IN_HOUR * SECONDS_IN_MINUTE
        private const val HOURS_IN_DAY = 24
        private const val DAY_IN_SECONDS = HOURS_IN_DAY * SECONDS_IN_HOUR
        private const val DAYS_IN_WEEK = 7
        private const val BI_WEEKLY_INTERVAL_DAYS = DAYS_IN_WEEK * 2

        // A university semester is ~14-16 weeks, so 12 alternating-week occurrences
        // (~6 months) comfortably covers a semester without needlessly consuming the
        // iOS-wide pending notification budget a year of occurrences would.
        private const val BI_WEEKLY_OCCURRENCE_COUNT = 12

        // iOS enforces a hard cap of 64 pending local notifications per app. Keep a
        // safety margin below it so scheduling for one event never starves others.
        private const val MAX_PENDING_NOTIFICATIONS_BUDGET = 60
    }
}
