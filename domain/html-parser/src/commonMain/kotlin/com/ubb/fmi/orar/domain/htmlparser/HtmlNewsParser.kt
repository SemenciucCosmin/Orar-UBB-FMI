package com.ubb.fmi.orar.domain.htmlparser

import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.SINGLE_QUOTE
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.domain.extensions.substringBetween
import com.ubb.fmi.orar.domain.htmlparser.model.HtmlArticle

object HtmlNewsParser {
    fun extractNews(html: String): List<HtmlArticle> {
        val postsHtml = html.split(POST_START_TAG).drop(1)
        return postsHtml.map { postHtml ->
            val detailsHtml = postHtml.substringBetween(DETAILS_START_TAG, DETAILS_END_TAG)
            val titleSectionHtml = postHtml.substringBetween(TITLE_SECTION_START_TAG, TITLE_SECTION_END_TAG)
            val textDataHtml = detailsHtml.substringBetween(TEXT_DATA_START_TAG, TEXT_DATA_END_TAG)

            val title = titleSectionHtml.substringBetween(TITLE_START_TAG, TITLE_END_TAG)
            val text = detailsHtml.substringBetween(TEXT_START_TAG, TEXT_END_TAG)
            val url = titleSectionHtml.substringBetween(POST_URL_START_TAG, POST_URL_END_TAG)
            val imageUrl = textDataHtml.substringBetween(POST_IMAGE_START_TAG, POST_IMAGE_END_TAG)
            val date = postHtml.substringBetween(DATE_START_TAG, DATE_END_TAG)

            val curatedTitle = title
                .replace(DASH_CODE, String.DASH)
                .replace(SINGLE_QUOTE_CODE, String.SINGLE_QUOTE)
                .replace(EMPHASIS_START_TAG, String.SPACE)
                .replace(EMPHASIS_END_TAG, String.BLANK)
                .replace(Regex(EXCESS_SPACES_REGEX), String.SPACE)
                .trim()

            val curatedText = text
                .replace(DASH_CODE, String.DASH)
                .replace(SINGLE_QUOTE_CODE, String.SINGLE_QUOTE)
                .replace(EMPHASIS_START_TAG, String.SPACE)
                .replace(EMPHASIS_END_TAG, String.BLANK)
                .replace(Regex(EXCESS_SPACES_REGEX), String.SPACE)
                .trim()

            HtmlArticle(
                title = curatedTitle,
                text = curatedText,
                url = url,
                imageUrl = imageUrl.ifBlank { null },
                date = date
            )
        }
    }

    private const val POST_START_TAG = "<div class=\"post-wrap clearfix\">"

    private const val DETAILS_START_TAG = "<div class=\"entry clearfix\">"
    private const val DETAILS_END_TAG = "<div class=\"readmore\">"

    private const val TITLE_SECTION_START_TAG = "<h2 class=\"title\">"
    private const val TITLE_SECTION_END_TAG = "</h2>"

    private const val TITLE_START_TAG = ">"
    private const val TITLE_END_TAG = "</a>"

    private const val POST_URL_START_TAG = "href=\""
    private const val POST_URL_END_TAG = "\""

    private const val POST_IMAGE_START_TAG = "src=\""
    private const val POST_IMAGE_END_TAG = "\""

    private const val TEXT_DATA_START_TAG =
        "<div style=\"float: left; margin-right: 16px\" class=\"thumbnail_container\">"
    private const val TEXT_DATA_END_TAG = "</div>"

    private const val TEXT_START_TAG = "</div>"
    private const val TEXT_END_TAG = "</div>"

    private const val DATE_START_TAG = "<span class=\"meta_date\">"
    private const val DATE_END_TAG = "</span>"

    private const val EMPHASIS_START_TAG = "<em>"
    private const val EMPHASIS_END_TAG = "</em>"

    private const val DASH_CODE = "&#8211;"
    private const val SINGLE_QUOTE_CODE = "&#8217;"
    private const val EXCESS_SPACES_REGEX = "\\s+"
}