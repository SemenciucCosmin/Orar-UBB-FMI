package com.ubb.fmi.orar.data.notifications.repository

import Logger
import com.ubb.fmi.orar.data.notifications.datasource.NotificationCacheDataSource
import com.ubb.fmi.orar.data.notifications.model.EventNotification
import com.ubb.fmi.orar.domain.calendar.usecase.GetUpcomingEventOccurrencesUseCase
import com.ubb.fmi.orar.domain.extensions.formatTime
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import kotlin.coroutines.resume
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

/**
 * iOS [NotificationRepository] implementation, backed by `UNUserNotificationCenter`.
 *
 * Every event is scheduled as a bounded batch of one-shot `UNTimeIntervalNotificationTrigger`
 * requests, one per upcoming teaching week occurrence. A repeating `UNCalendarNotificationTrigger`
 * would be cheaper, but iOS offers no way to suspend one for the weeks it must stay silent, so it
 * would keep firing right through the winter, Easter, exam and summer periods.
 *
 * Because iOS caps an app at 64 pending local notifications, the batch is sized from what's left
 * of that budget divided across the events being scheduled, and topped up again every time the
 * app re-syncs its notifications.
 */
class NotificationRepositoryImpl(
    private val notificationCacheDataSource: NotificationCacheDataSource,
    private val getUpcomingEventOccurrencesUseCase: GetUpcomingEventOccurrencesUseCase,
    private val logger: Logger,
) : NotificationRepository {

    override suspend fun schedule(notifications: List<EventNotification>) {
        if (notifications.isEmpty()) return

        val occurrenceCount = resolveOccurrencesPerEvent(notifications)
        if (occurrenceCount <= 0) {
            logger.e(TAG, "Skipped scheduling ${notifications.size} event(s): pending notification budget exhausted")
            return
        }

        val nowMillis = Clock.System.now().toEpochMilliseconds()
        notifications.forEach { notification ->
            schedule(notification, occurrenceCount, nowMillis)
        }
    }

    /**
     * Replaces everything pending for [notification] with its next [occurrenceCount] teaching
     * week occurrences.
     */
    private fun schedule(notification: EventNotification, occurrenceCount: Int, nowMillis: Long) {
        // Clear every identifier this event may have used before. Without this, an event whose
        // frequency or lead time changed would leave its old requests pending alongside the new
        // ones, firing at both times and wasting the app-wide pending notification budget.
        notificationCacheDataSource
            .getNotificationCenter()
            .removePendingNotificationRequestsWithIdentifiers(allNotificationIdentifiers(notification.id))

        val occurrencesMillis = getUpcomingEventOccurrencesUseCase(
            semester = notification.academicSemester,
            day = notification.day,
            frequency = notification.frequency,
            startHour = notification.startHour,
            startMinute = notification.startMinute,
            advanceMinutes = notification.advanceMinutes,
            afterMillis = nowMillis,
            limit = occurrenceCount,
        )
        if (occurrencesMillis.isEmpty()) {
            logger.d(TAG, "Skipped notification ${notification.id}, no teaching week left to fire in")
            return
        }

        logger.d(
            TAG,
            "Scheduling ${occurrencesMillis.size}/$occurrenceCount occurrence(s) of ${notification.id}, " +
                "first: ${Instant.fromEpochMilliseconds(occurrencesMillis.first())}, " +
                "last: ${Instant.fromEpochMilliseconds(occurrencesMillis.last())}, " +
                "advanceMinutes: ${notification.advanceMinutes}, semester: ${notification.academicSemester?.index}",
        )

        val content = buildNotificationContent(notification)
        occurrencesMillis.forEachIndexed { occurrenceIndex, occurrenceMillis ->
            val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(
                timeInterval = (occurrenceMillis - nowMillis).milliseconds.inWholeSeconds.toDouble(),
                repeats = false,
            )

            val request = UNNotificationRequest.requestWithIdentifier(
                identifier = occurrenceIdentifier(notification.id, occurrenceIndex),
                content = content,
                trigger = trigger,
            )

            addNotificationRequest(request)
        }
    }

    override suspend fun cancel(id: String) {
        notificationCacheDataSource
            .getNotificationCenter()
            .removePendingNotificationRequestsWithIdentifiers(allNotificationIdentifiers(id))
        logger.d(TAG, "Cancelled notification $id")
    }

    /**
     * Builds the notification title/body shown to the user from [notification]'s details.
     */
    private fun buildNotificationContent(notification: EventNotification): UNMutableNotificationContent {
        val startTimeLabel = formatTime(notification.startHour, notification.startMinute)
        val endTimeLabel = formatTime(notification.endHour, notification.endMinute)
        val deepLink = "$USER_TIMETABLE_DEEP_LINK_BASE?eventId=${notification.id}"

        return UNMutableNotificationContent().apply {
            setTitle("${notification.activity} • $startTimeLabel - $endTimeLabel")
            setBody("${notification.type.id} • ${notification.participant} • ${notification.location}")
            setUserInfo(mapOf(DEEP_LINK_USER_INFO_KEY to deepLink))
        }
    }

    /**
     * Decides how many future occurrences each event of the batch may claim.
     *
     * iOS silently drops local notification requests past 64 pending ones app-wide, so the
     * remaining budget is split evenly instead of letting the first events of the batch consume
     * all of it and leave the rest with nothing.
     */
    private suspend fun resolveOccurrencesPerEvent(notifications: List<EventNotification>): Int {
        val ownedCount = countPendingNotifications(notifications.map { it.id }.toSet())
        val pendingCount = countPendingNotifications(ids = null)

        // Requests this batch already owns are about to be replaced, so they are free to reuse.
        val availableBudget = (MAX_PENDING_NOTIFICATIONS_BUDGET - pendingCount + ownedCount).coerceAtLeast(0)
        val perEvent = availableBudget / notifications.size

        logger.d(
            TAG,
            "Budget for ${notifications.size} event(s): pending=$pendingCount, owned=$ownedCount, " +
                "available=$availableBudget, perEvent=$perEvent",
        )

        if (perEvent < MAX_OCCURRENCES_PER_EVENT) {
            logger.i(
                TAG,
                "Limited ${notifications.size} event(s) to $perEvent occurrence(s) each " +
                    "(pending=$pendingCount, budget=$MAX_PENDING_NOTIFICATIONS_BUDGET)",
            )
        }

        return minOf(MAX_OCCURRENCES_PER_EVENT, perEvent)
    }

    /**
     * Returns how many local notifications are currently pending for this app, either in total
     * ([ids] `null`) or only those belonging to the given event ids.
     */
    private suspend fun countPendingNotifications(ids: Set<String>?): Int = suspendCancellableCoroutine { cont ->
        notificationCacheDataSource.getNotificationCenter().getPendingNotificationRequestsWithCompletionHandler {
            val requests = it.orEmpty().filterIsInstance<UNNotificationRequest>()
            val count = when (ids) {
                null -> requests.size
                else -> requests.count { request -> eventIdOf(request.identifier) in ids }
            }

            cont.resume(count)
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
            } else {
                logger.d(TAG, "Scheduled notification ${request.identifier}")
            }
        }
    }

    private fun occurrenceIdentifier(id: String, occurrenceIndex: Int) = "$id$OCCURRENCE_SEPARATOR$occurrenceIndex"

    /** Recovers the event id an occurrence identifier was built from. */
    private fun eventIdOf(identifier: String) = identifier.substringBefore(OCCURRENCE_SEPARATOR)

    /**
     * Returns every identifier ever used for [id]'s notifications (every possible occurrence
     * slot, plus the bare id and the legacy alternating-week slots earlier versions used), so
     * pending/stale requests are fully cleared regardless of how many occurrences were actually
     * scheduled at the time, or by which app version.
     */
    private fun allNotificationIdentifiers(id: String): List<String> {
        val occurrenceIdentifiers = List(MAX_OCCURRENCES_PER_EVENT) { index -> occurrenceIdentifier(id, index) }
        val legacyIdentifiers = List(LEGACY_BI_WEEKLY_OCCURRENCE_COUNT) { index ->
            "$id$LEGACY_BI_WEEKLY_SEPARATOR$index"
        }

        return listOf(id) + occurrenceIdentifiers + legacyIdentifiers
    }

    companion object {
        private const val TAG = "NotificationRepository"

        // iOS enforces a hard cap of 64 pending local notifications per app. Keep a
        // safety margin below it so scheduling for one event never starves others.
        private const val MAX_PENDING_NOTIFICATIONS_BUDGET = 60

        // Upper bound on how far ahead a single event is armed. The app tops every event back
        // up whenever it re-syncs notifications, so this only has to outlast the gap between
        // two launches, not a whole semester.
        private const val MAX_OCCURRENCES_PER_EVENT = 8

        private const val OCCURRENCE_SEPARATOR = "_occurrence_"

        // Identifier scheme used before notifications were bound to teaching weeks. Kept only so
        // requests an older install left pending are cleared on upgrade instead of firing
        // alongside the new ones.
        private const val LEGACY_BI_WEEKLY_SEPARATOR = "_bw_"
        private const val LEGACY_BI_WEEKLY_OCCURRENCE_COUNT = 12

        private const val DEEP_LINK_USER_INFO_KEY = "deep_link"
        private const val USER_TIMETABLE_DEEP_LINK_BASE = "orarubbfmi://user-timetable"
    }
}
