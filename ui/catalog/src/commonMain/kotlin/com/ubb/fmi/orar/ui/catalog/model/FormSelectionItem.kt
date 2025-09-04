package com.ubb.fmi.orar.ui.catalog.model

/**
 * Represents an item in a form selection with an identifier and a label.
 *
 * @param T The type of the identifier.
 * @property id The unique identifier for the form selection item.
 * @property label The text label displayed for the form selection item.
 */
data class FormSelectionItem<T>(
    val id: T,
    val label: String,
)
