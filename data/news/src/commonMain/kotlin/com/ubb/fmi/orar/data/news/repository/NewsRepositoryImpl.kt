package com.ubb.fmi.orar.data.news.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.model.isSuccess
import com.ubb.fmi.orar.data.news.datasource.NewsDataSource
import com.ubb.fmi.orar.data.news.model.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Repository for managing data flow and data source for rooms
 */
class NewsRepositoryImpl(
    private val coroutineScope: CoroutineScope,
    private val newsDataSource: NewsDataSource,
) : NewsRepository {

    private val articlesFlow: MutableStateFlow<Resource<List<Article>>> = MutableStateFlow(
        Resource(null, Status.Loading)
    )

    init {
        prefetchNews()
        initializeNews()
    }

    /**
     * Retrieves a [Flow] of list of articles
     */
    override fun getNews(): Flow<Resource<List<Article>>> {
        return articlesFlow
    }

    /**
     * Invalidates news cache
     */
    override suspend fun invalidate() {
        newsDataSource.invalidate()
    }

    /**
     * Tries prefetching the rooms from API for a safety update of local data
     */
    private fun prefetchNews() {
        coroutineScope.launch { getNewsFromApi() }
    }

    /**
     * Initializes collection of database entries and possible API updates
     */
    private fun initializeNews() {
        coroutineScope.launch { getNewsFromCache() }
    }

    /**
     * Provides the collection of database data flow
     */
    private suspend fun getNewsFromCache() {
        newsDataSource.getNewsFromCache().collectLatest { articles ->
            when {
                articles.isEmpty() -> getNewsFromApi()
                else -> articlesFlow.update { Resource(articles, Status.Success) }
            }
        }
    }

    /**
     * Retrieves articles from API and update the database of output flow
     */
    private suspend fun getNewsFromApi() {
        val resource = newsDataSource.getNewsFromApi()
        val articles = resource.payload

        when {
            resource.status.isSuccess() && articles != null -> {
                newsDataSource.saveNewsInCache(articles)
            }

            else -> articlesFlow.update {
                when {
                    it.payload.isNullOrEmpty() -> Resource(null, resource.status)
                    else -> it
                }
            }
        }
    }
}