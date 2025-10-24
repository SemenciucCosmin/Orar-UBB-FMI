package com.ubb.fmi.orar.feature.news.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.network.model.isLoading
import com.ubb.fmi.orar.data.news.model.ArticleType
import com.ubb.fmi.orar.data.news.repository.NewsRepository
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.analytics.AnalyticsLogger
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent
import com.ubb.fmi.orar.domain.usertimetable.model.UserType
import com.ubb.fmi.orar.feature.news.ui.viewmodel.model.NewsUiState
import com.ubb.fmi.orar.ui.catalog.extensions.toErrorStatus
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the state of the news screen.
 */
class NewsViewModel(
    private val newsRepository: NewsRepository,
    private val timetablePreferences: TimetablePreferences,
    private val analyticsLogger: AnalyticsLogger,
    private val logger: Logger,
) : ViewModel() {

    /**
     * A Job that represents the ongoing data fetch operation for news.
     * It can be cancelled to stop the operation if needed.
     */
    private var job: Job

    /**
     * A MutableStateFlow that holds the current UI state of the news screen.
     * It is updated with loading, error, and news data as it becomes available.
     */
    private val _uiState = MutableStateFlow(NewsUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    /**
     * Initializes the ViewModel by starting the data fetch operation for news.
     */
    init {
        setArticleFilter()
        job = getNews()
    }

    /**
     * Fetches the list of news from the data source and updates the UI state.
     */
    private fun getNews() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorStatus = null) }
        newsRepository.getNews().collectLatest { resource ->
            logger.d(TAG, "getNews resource: $resource")
            _uiState.update {
                it.copy(
                    articles = resource.payload?.toImmutableList() ?: persistentListOf(),
                    isLoading = resource.status.isLoading(),
                    errorStatus = resource.status.toErrorStatus(),
                )
            }
        }
    }

    private fun setArticleFilter() {
        viewModelScope.launch {
            timetablePreferences.getConfiguration().filterNotNull().collectLatest { config ->
                val selectedArticleType = when (config.userTypeId) {
                    UserType.STUDENT.id -> ArticleType.STUDENT
                    else -> ArticleType.TEACHER
                }

                _uiState.update { it.copy(selectedArticleType = selectedArticleType) }
            }
        }
    }

    /**
     * Retries the data fetch operation for news by cancelling the current job
     * and starting a new one. This is useful in case of an error or when the user
     * wants to refresh the news list.
     */
    fun retry() {
        logger.d(TAG, "retry")
        job.cancel()
        job = getNews()
    }

    /**
     * Registers analytics event for the item click action
     */
    fun handleClickAction() {
        when (_uiState.value.selectedArticleType) {
            ArticleType.STUDENT -> analyticsLogger.logEvent(AnalyticsEvent.NEWS_ACCESS_STUDENT)
            ArticleType.TEACHER -> analyticsLogger.logEvent(AnalyticsEvent.NEWS_ACCESS_TEACHER)
        }
    }

    companion object {
        private const val TAG = "NewsViewModel"
    }
}
