package com.ubb.fmi.orar.domain.extensions

/**
 * String extension for empty
 */
val String.Companion.BLANK: String
    get() = ""

/**
 * String extension for quote sign
 */
val String.Companion.QUOTE: String
    get() = "\""

/**
 * String extension for space
 */
val String.Companion.SPACE: String
    get() = " "

/**
 * String extension for equal sign
 */
val String.Companion.EQUAL: String
    get() = "="

/**
 * String extension for slash sign
 */
val String.Companion.SLASH: String
    get() = "/"

/**
 * String extension for pipe sign
 */
val String.Companion.PIPE: String
    get() = "|"

/**
 * String extension for dash sign
 */
val String.Companion.DASH: String
    get() = "-"

/**
 * String extension for colon sign
 */
val String.Companion.COLON: String
    get() = ":"

/**
 * String extension for comma sign
 */
val String.Companion.COMMA: String
    get() = ","

/**
 * String extension for dot sign
 */
val String.Companion.DOT: String
    get() = "."

/**
 * String extension for asterisk sign
 */
val String.Companion.ASTERISK: String
    get() = "*"

/**
 * String extension for single quote sign
 */
val String.Companion.SINGLE_QUOTE: String
    get() = "'"

/**
 * Returns substring between [afterDelimiter] and [beforeDelimiter]
 */
fun String.substringBetween(
    afterDelimiter: String,
    beforeDelimiter: String,
) = this.substringAfter(afterDelimiter).substringBefore(beforeDelimiter)