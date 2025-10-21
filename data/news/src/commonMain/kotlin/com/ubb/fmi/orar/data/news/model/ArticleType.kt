package com.ubb.fmi.orar.data.news.model

/**
 * Represents the type of article
 */
enum class ArticleType(val id: String) {
    STUDENT(id = "student"),
    TEACHER(id = "teacher");

    companion object {
        fun getById(id: String): ArticleType {
            return entries.firstOrNull { it.id == id } ?: STUDENT
        }
    }
}