package com.ubb.fmi.orar.domain.extensions

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun Long.formatToDate(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date

    val day = localDate.day.toString().padStart(2, '0')
    val month = localDate.day.toString().padStart(2, '0')
    val year = localDate.year.toString()

    return "$day${String.DOT}$month${String.DOT}$year"
}