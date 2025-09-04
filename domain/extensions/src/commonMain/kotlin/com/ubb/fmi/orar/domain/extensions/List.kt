package com.ubb.fmi.orar.domain.extensions

/**
 * Retrieves the element in the middle of the list or throws
 * [NoSuchElementException] if the list is empty
 */
fun <T> List<T>.middle(): T {
    if (isEmpty()) {
        throw NoSuchElementException("List is empty.")
    }
    return this[this.size / 2]
}
