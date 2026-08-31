package com.ubb.fmi.orar.data.notifications.repository

import com.ubb.fmi.orar.data.notifications.datasource.NotificationCacheDataSource
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.notifications.model.ClassNotification
import com.ubb.fmi.orar.data.notifications.preferences.NotificationPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarIdentifierGregorian
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitWeekday
import platform.Foundation.NSCalendarUnitWeekOfYear
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

class NotificationRepositoryImpl(
    private val notificationCacheDataSource: NotificationCacheDataSource,
    private val notificationPreferences: NotificationPreferences
) : NotificationRepository {

    override suspend fun schedule(notification: ClassNotification) {
        requestAuthorizationIfNeeded()

        val content = UNMutableNotificationContent().apply {
            setTitle(notification.className)
            val timeLabel = "%02d:%02d".format(notification.hour, notification.minute)
            setBody("${notification.classType.id} • $timeLabel")
        }

        when (notification.frequency) {
            Frequency.BOTH -> {
                val request = UNNotificationRequest.requestWithIdentifier(
                    identifier = notification.id,
                    content = content,
                    trigger = buildWeeklyTrigger(notification),
                )
                notificationCacheDataSource
                    .getNotificationCenter()
                    .addNotificationRequest(request, withCompletionHandler = null)
            }
            else -> {
                // Pre-schedule BI_WEEKLY_COUNT occurrences since UNCalendarNotificationTrigger
                // cannot express bi-weekly patterns natively.
                val firstOccurrenceSeconds = secondsUntilFirstBiWeeklyOccurrence(notification)
                repeat(BI_WEEKLY_COUNT) { i ->
                    val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(
                        timeInterval = firstOccurrenceSeconds + i * 14 * DAY_IN_SECONDS,
                        repeats = false,
                    )
                    val request = UNNotificationRequest.requestWithIdentifier(
                        identifier = biWeeklyIdentifier(notification.id, i),
                        content = content,
                        trigger = trigger,
                    )
                    notificationCacheDataSource.getNotificationCenter().addNotificationRequest(request, withCompletionHandler = null)
                }
            }
        }

        notificationPreferences.addScheduledId(notification.id)
    }

    override suspend fun cancel(id: String) {
        notificationCacheDataSource.getNotificationCenter().removePendingNotificationRequestsWithIdentifiers(allIdentifiers(id))
        notificationPreferences.removeScheduledId(id)
    }

    override suspend fun cancelAll() {
        notificationCacheDataSource.getNotificationCenter().removeAllPendingNotificationRequests()
        notificationPreferences.clearScheduledIds()
    }

    private suspend fun requestAuthorizationIfNeeded() {
        suspendCancellableCoroutine { cont ->
            notificationCacheDataSource.getNotificationCenter().requestAuthorizationWithOptions(
                options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge,
            ) { _, _ -> cont.resume(Unit) }
        }
    }

    private fun buildWeeklyTrigger(notification: ClassNotification): UNCalendarNotificationTrigger {
        val components = NSDateComponents().apply {
            weekday = notification.day.toIosWeekday().toLong()
            hour = notification.hour.toLong()
            minute = notification.minute.toLong()
            second = 0
        }
        return UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
            dateComponents = components,
            repeats = true,
        )
    }

    private fun biWeeklyIdentifier(id: String, index: Int) = "${id}_bw_$index"

    private fun allIdentifiers(id: String): List<String> =
        listOf(id) + List(BI_WEEKLY_COUNT) { i -> biWeeklyIdentifier(id, i) }

    private fun secondsUntilFirstBiWeeklyOccurrence(notification: ClassNotification): Double {
        val calendar = NSCalendar(calendarIdentifier = NSCalendarIdentifierGregorian)
        val now = NSDate()
        val currentComponents = calendar.components(
            unitFlags = NSCalendarUnitWeekday or NSCalendarUnitWeekOfYear or NSCalendarUnitHour or NSCalendarUnitMinute,
            fromDate = now,
        )
        val targetWeekday = notification.day.toIosWeekday()
        val currentWeekday = currentComponents.weekday.toInt()
        val currentHour = currentComponents.hour.toInt()
        val currentMinute = currentComponents.minute.toInt()

        var daysUntil = (targetWeekday - currentWeekday + 7) % 7
        if (daysUntil == 0 &&
            (notification.hour < currentHour || (notification.hour == currentHour && notification.minute <= currentMinute))
        ) {
            daysUntil = 7
        }

        val secondsToDay = daysUntil * DAY_IN_SECONDS
        val targetDateEstimate = NSDate(timeIntervalSinceNow = secondsToDay)
        val targetWeekComponents = calendar.components(NSCalendarUnitWeekOfYear, fromDate = targetDateEstimate)
        val targetWeek = targetWeekComponents.weekOfYear.toInt()

        val isOddWeek = targetWeek % 2 != 0
        val needsOddWeek = notification.frequency == Frequency.WEEK_1
        val extraWeekSeconds = if (isOddWeek != needsOddWeek) DAY_IN_SECONDS * 7 else 0.0

        val timeOfDaySeconds = notification.hour * 3600.0 + notification.minute * 60.0
        val currentTimeOfDaySeconds = currentHour * 3600.0 + currentMinute * 60.0

        return secondsToDay + extraWeekSeconds + timeOfDaySeconds - currentTimeOfDaySeconds
    }

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
        private const val DAY_IN_SECONDS = 24.0 * 60 * 60
        /** Pre-scheduled bi-weekly occurrences (~1 year). iOS allows max 64 pending notifications. */
        private const val BI_WEEKLY_COUNT = 26
    }
}
