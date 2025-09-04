package com.ubb.fmi.orar.domain.htmlparser.model

/**
 * Data class for a html table
 * @param [title]: first header body above the table that acts as its title
 * @param [rows]: list of all [Row] from that table
 */
data class Table(
    val title: String,
    val rows: List<Row>
)
