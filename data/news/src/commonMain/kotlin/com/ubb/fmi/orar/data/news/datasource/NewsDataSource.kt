package com.ubb.fmi.orar.data.news.datasource

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.news.model.Article
import kotlinx.coroutines.flow.Flow

/**
 * Data source for managing news related information
 */
interface NewsDataSource {

    /**
     * Retrieves [Flow] of articles from database
     */

    suspend fun getNewsFromCache(): Flow<List<Article>>

    /**
     * Saves [articles] in database
     */
    suspend fun saveNewsInCache(
        articles: List<Article>
    )

    suspend fun getNewsFromApi(): Resource<List<Article>>

    /**
     * Invalidates all cached articles
     */
    suspend fun invalidate()
}
