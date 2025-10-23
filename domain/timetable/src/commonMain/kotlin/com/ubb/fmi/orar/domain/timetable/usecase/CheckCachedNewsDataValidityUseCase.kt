package com.ubb.fmi.orar.domain.timetable.usecase

import Logger
import com.ubb.fmi.orar.data.news.repository.NewsRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Use case for checking the validity of cached news data
 * Deleted news data older than 6 months
 */
class CheckCachedNewsDataValidityUseCase(
    private val newsRepository: NewsRepository,
    private val logger: Logger,
) {
    /**
     * Checks the validity of cached news data and invalidates entries older than 6 months
     */
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke() {
        try {
            logger.d(TAG, "invalidate")
            val currentMillis = Clock.System.now().toEpochMilliseconds()
            val timestampLimit = currentMillis - VALID_DATA_TIMEFRAME
            newsRepository.invalidate(timestampLimit)
        } catch (exception: Exception) {
            logger.d(TAG, "invalidate failure: ${exception.message}")
        }
    }

    companion object {
        private const val TAG = "CheckCachedNewsDataValidityUseCase"
        private const val VALID_DATA_TIMEFRAME = 6L * 30 * 24 * 60 * 60 * 1000 // 6 months in millis
    }
}
