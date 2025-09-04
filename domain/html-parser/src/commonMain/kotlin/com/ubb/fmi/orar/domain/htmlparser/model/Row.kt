package com.ubb.fmi.orar.domain.htmlparser.model

/**
 * Data class for a single row from a html table
 * @param [cells]: list of all [Cell] from that table row
 */
data class Row(
    val cells: List<Cell>
)
