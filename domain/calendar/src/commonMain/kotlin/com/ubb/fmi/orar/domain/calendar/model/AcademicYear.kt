package com.ubb.fmi.orar.domain.calendar.model

/**
 * The full teaching calendar of a single UBB academic year, running from the Monday closest to
 * October 1st of [startYear] until the next academic year begins. [startMillis] and the exclusive
 * [endMillis] are epoch milliseconds, as is every moment this calendar is queried with.
 *
 * This is the single source of truth for every date question the app asks: which semester a date
 * belongs to, which numbered teaching week it falls in, whether that week is odd or even, and
 * whether there is any teaching going on at all. Notification scheduling walks [teachingWeeks]
 * directly, which is what keeps notifications silent during the winter, Easter, exam, resit and
 * summer periods.
 */
data class AcademicYear(
    val startYear: Int,
    val startMillis: Long,
    val endMillis: Long,
    val semesters: List<AcademicSemester>,
) {
    /**
     * Returns the semester [millis] falls in, or `null` when it lands outside both semesters'
     * teaching spans (exam/resit sessions, the inter-semester break or the summer break).
     */
    fun getSemesterAt(millis: Long): AcademicSemester? {
        return semesters.firstOrNull { millis in it }
    }
}
