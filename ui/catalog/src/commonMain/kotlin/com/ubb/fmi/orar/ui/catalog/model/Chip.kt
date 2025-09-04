package com.ubb.fmi.orar.ui.catalog.model

/**
 * Represents a chip with an identifier and a label.
 *
 * @param T The type of the identifier.
 * @property id The unique identifier for the chip.
 * @property label The text label displayed on the chip.
 */
data class Chip<T>(
    val id: T,
    val label: String,
)
