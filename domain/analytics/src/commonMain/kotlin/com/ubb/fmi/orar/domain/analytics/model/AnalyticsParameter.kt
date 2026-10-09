package com.ubb.fmi.orar.domain.analytics.model

/**
 * Parameter keys attached to [AnalyticsEvent]s. Kept short and snake_case, as Firebase limits
 * parameter names to 40 alphanumeric/underscore characters.
 */
object AnalyticsParameter {
    const val TIMETABLE_TYPE = "timetable_type"
    const val ERROR_TYPE = "error_type"
    const val FREQUENCY = "frequency"
    const val EVENT_TYPE = "event_type"
    const val ADVANCE_MINUTES = "advance_minutes"
    const val THEME = "theme"
    const val USER_TYPE = "user_type"
}

/**
 * Values for [AnalyticsParameter.TIMETABLE_TYPE].
 */
object AnalyticsTimetableType {
    const val USER = "user"
    const val GROUP = "group"
    const val TEACHER = "teacher"
    const val SUBJECT = "subject"
    const val ROOM = "room"
}
