package com.ubb.fmi.orar.domain.extensions

fun formatTime(hour: Int, minute: Int): String {
    val hourString = hour.coerceAtLeast(0).toString().padStart(2, '0')
    val minuteString = minute.coerceAtLeast(0).toString().padStart(2, '0')
    return "$hourString${String.COLON}$minuteString"
}
