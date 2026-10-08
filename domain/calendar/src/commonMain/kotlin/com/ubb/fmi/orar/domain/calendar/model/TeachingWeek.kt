package com.ubb.fmi.orar.domain.calendar.model

/**
 * A single numbered week of teaching activities inside a semester, spanning from [startMillis]
 * (Monday at midnight) up to, but excluding, [endMillis] (the following Monday at midnight). Both
 * are epoch milliseconds in the time zone the calendar was built for.
 *
 * [index] is the 1-based teaching week index within its semester (1..14). Weeks lost to the
 * winter or Easter break do not consume a number, so the odd/even rhythm the "sapt. 1"/"sapt. 2"
 * frequencies alternate on keeps running across a break instead of being shifted by it.
 */
data class TeachingWeek(
    val index: Int,
    val startMillis: Long,
    val endMillis: Long,
) {
    /** Whether this is a "sapt. 1" week (teaching weeks 1, 3, 5, ...). */
    val isOdd: Boolean get() = index % 2 != 0

    operator fun contains(millis: Long): Boolean = millis in startMillis..<endMillis
}
