package com.ubb.fmi.orar.data.notifications.repository

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

    private fun scheduleBiWeeklyNotification(
        notification: EventNotification,
        content: UNMutableNotificationContent,
    ) {
        val firstOccurrenceSeconds = secondsUntilFirstBiWeeklyOccurrence(notification)

        repeat(BI_WEEKLY_OCCURRENCE_COUNT) { occurrenceIndex ->
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

    private fun addNotificationRequest(request: UNNotificationRequest) {
        notificationCacheDataSource.getNotificationCenter().addNotificationRequest(
            request = request,
            withCompletionHandler = null
        )
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
        private const val MINUTES_IN_HOUR = 60
        private const val SECONDS_IN_MINUTE = 60.0
        private const val SECONDS_IN_HOUR = MINUTES_IN_HOUR * SECONDS_IN_MINUTE
        private const val HOURS_IN_DAY = 24
        private const val DAY_IN_SECONDS = HOURS_IN_DAY * SECONDS_IN_HOUR
        private const val DAYS_IN_WEEK = 7
        private const val BI_WEEKLY_INTERVAL_DAYS = DAYS_IN_WEEK * 2
        private const val BI_WEEKLY_OCCURRENCE_COUNT = 26
    }
}
