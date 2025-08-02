package com.ubb.fmi.orar.domain.extensions

fun <T> List<T>.middle(): T {
    if (isEmpty())
        throw NoSuchElementException("List is empty.")
    return this[this.size / 2]
}
