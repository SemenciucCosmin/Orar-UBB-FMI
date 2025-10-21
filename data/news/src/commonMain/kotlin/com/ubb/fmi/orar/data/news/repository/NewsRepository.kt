package com.ubb.fmi.orar.data.news.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.news.model.Article
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing data flow and data source for news
 */
interface NewsRepository {

    /**
     * Retrieves a [Flow] of list of articles
     */
    fun getNews(): Flow<Resource<List<Article>>>

    /**
     * Invalidates news cache
     */
    suspend fun invalidate()
}