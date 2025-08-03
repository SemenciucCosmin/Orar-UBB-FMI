package com.ubb.fmi.orar.domain.htmlparser

import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.EQUAL
import com.ubb.fmi.orar.domain.extensions.QUOTE
import com.ubb.fmi.orar.domain.extensions.SLASH
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.domain.extensions.middle
import com.ubb.fmi.orar.domain.htmlparser.model.Cell
import com.ubb.fmi.orar.domain.htmlparser.model.Row
import com.ubb.fmi.orar.domain.htmlparser.model.Table

private const val INVALID_INDEX = -1

private const val NON_BREAKING_SPACE = "&nbsp;"

private const val TAG_RIGHT = ">"
private const val TAG_LEFT = "<"

private const val HEADLINE_TAG_OPEN = "<h1"
private const val HEADLINE_TAG_CLOSED = "</h1"

private const val HYPERLINK_TAG_OPEN = "<a"
private const val HYPERLINK_TAG_CLOSED = "</a"

private const val NESTED_HYPERLINK_TAG_CLOSED = "</a></a>"

private const val TABLE_TAG_OPEN = "<table"
private const val TABLE_TAG_CLOSED = "</table"

private const val TABLE_ROW_TAG_OPEN = "<tr"
private const val TABLE_ROW_TAG_CLOSED = "</tr"

private const val TABLE_COLUMN_TAG_OPEN = "<td"
private const val TABLE_COLUMN_TAG_CLOSED = "</td"

private const val HREF_TAG_OPEN = "f=\""
private const val HREF_TAG_CLOSED = "\" >"

private const val HTML_EXTENSION = ".html"
private const val NULL = "null"

object HtmlParser {

    fun extractTables(html: String): List<Table> {
        val headers = html.select(HEADLINE_TAG_OPEN, HEADLINE_TAG_CLOSED).drop(1).map {
            it.select(TAG_RIGHT, TAG_LEFT)
        }.flatten()

        val tablesHtml = html.select(TABLE_TAG_OPEN, TABLE_TAG_CLOSED).map { tableHtml ->
            tableHtml.select(TABLE_ROW_TAG_OPEN, TABLE_ROW_TAG_CLOSED).map { rowHtml ->
                rowHtml.select(TABLE_COLUMN_TAG_OPEN, TABLE_COLUMN_TAG_CLOSED).map { cellHtml ->
                    val id = cellHtml
                        .select(HREF_TAG_OPEN, HREF_TAG_CLOSED)
                        .firstOrNull()?.formatHyperlinkId() ?: String.BLANK

                    val cells = when {
                        !cellHtml.contains(HYPERLINK_TAG_OPEN) -> {
                            cellHtml.select(TAG_RIGHT, TAG_LEFT)
                        }

                        cellHtml.contains(NESTED_HYPERLINK_TAG_CLOSED) -> {
                            val startIndex = cellHtml.lastIndexOf(HYPERLINK_TAG_OPEN)
                            val endIndex = cellHtml.lastIndexOf(HYPERLINK_TAG_CLOSED)

                            cellHtml
                                .substring(startIndex + 1, endIndex -1)
                                .select(TAG_RIGHT, TAG_LEFT)
                        }

                        else -> {
                            cellHtml
                                .select(HYPERLINK_TAG_OPEN, HYPERLINK_TAG_CLOSED)
                                .firstOrNull()?.select(TAG_RIGHT, TAG_LEFT) ?: emptyList()
                        }
                    }

                    id to cells.middle()
                }.map { (id, value) ->
                    when {
                        value == NON_BREAKING_SPACE -> id to NULL
                        else -> id to value
                    }
                }
            }.filter { it.isNotEmpty() }
        }

        val tables = tablesHtml.mapIndexed { index, tableHtml ->
            Table(
                title = headers.getOrNull(index) ?: String.BLANK,
                rows = tableHtml.map { rowHtml ->
                    Row(
                        cells = rowHtml.map { (id, value) ->
                            Cell(
                                id = id,
                                value = value
                            )
                        }
                    )
                }
            )
        }

        return tables
    }

    private fun String.formatHyperlinkId(): String {
        val lastSegmentIndex = this.indexOfLast { it.toString() == String.SLASH } + 1
        val hyperlinkId = when {
            lastSegmentIndex == -1 -> this
            else -> this.substring(lastSegmentIndex)
        }

        return hyperlinkId
            .replace(String.QUOTE, String.BLANK)
            .replace(String.EQUAL, String.BLANK)
            .replace(HTML_EXTENSION, String.BLANK)
            .replace(String.SPACE, String.BLANK)
    }

    private fun String.select(openTag: String, closedTag: String): List<String> {
        var html = this
        val items = mutableListOf<String>()

        var startIndex = html.indexOf(openTag)
        var endIndex = html.indexOf(closedTag) + closedTag.length - 1

        while (startIndex != INVALID_INDEX && endIndex != INVALID_INDEX) {
            items.add(html.substring(startIndex + 1, endIndex))

            html = html.substring(endIndex, html.lastIndex + 1)

            startIndex = html.indexOf(openTag)
            endIndex = html.indexOf(closedTag) + closedTag.length - 1
        }

        return items
    }
}
