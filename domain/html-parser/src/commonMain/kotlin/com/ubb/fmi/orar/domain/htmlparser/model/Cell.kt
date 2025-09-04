package com.ubb.fmi.orar.domain.htmlparser.model

/**
 * Data class for a single cell from a html table
 * @param [id]: id of the cell from the href body
 * @param [value]: text value of the cell
 */
data class Cell(
    val id: String,
    val value: String
)
