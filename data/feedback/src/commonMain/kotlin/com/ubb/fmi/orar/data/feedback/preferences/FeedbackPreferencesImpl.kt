package com.ubb.fmi.orar.data.feedback.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import com.ubb.fmi.orar.data.feedback.model.FeedbackMetrics
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

/**
 * Preferences for feedback feature
 */
class FeedbackPreferencesImpl(
    private val dataStore: DataStore<Preferences>
) : FeedbackPreferences {

    /**
     * Gets all feedback metrics
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getFeedbackMetrics(): Flow<FeedbackMetrics> {
        return dataStore.data.mapLatest { preferences ->
            val isFeedbackLoopShown = preferences[IS_FEEDBACK_LOOP_SHOWN] == true
            val firstUsageTimestamp = preferences[FIRST_USAGE_TIMESTAMP]
            val postponedTimestamp = preferences[POSTPONED_TIMESTAMP] ?: DEFAULT_TIMESTAMP
            val appUsagePoints = preferences[APP_USAGE_POINTS] ?: DEFAULT_POINTS

            FeedbackMetrics(
                isFeedbackLoopShown = isFeedbackLoopShown,
                firstUsageTimestamp = firstUsageTimestamp,
                postponedTimestamp = postponedTimestamp,
                appUsagePoints = appUsagePoints
            )
        }
    }

    override suspend fun setFeedbackLoopShown() {
        dataStore.edit { it[IS_FEEDBACK_LOOP_SHOWN] = true }
    }

    /**
     * Sets first app usage timestamp
     */
    override suspend fun setFirstUsageTimestamp(value: Long) {
        dataStore.edit { it[FIRST_USAGE_TIMESTAMP] = value }
    }

    /**
     * Sets postponed feed back loop timestamp
     */
    override suspend fun setPostponedTimestamp(value: Long) {
        dataStore.edit { it[POSTPONED_TIMESTAMP] = value }
    }

    /**
     * Sets points of app usage
     */
    override suspend fun setAppUsagePoints(value: Int) {
        dataStore.edit { it[APP_USAGE_POINTS] = value }
    }

    companion object {
        private const val DEFAULT_POINTS = 0
        private const val DEFAULT_TIMESTAMP = 0L
        private val IS_FEEDBACK_LOOP_SHOWN = booleanPreferencesKey("IS_FEEDBACK_LOOP_SHOWN")
        private val FIRST_USAGE_TIMESTAMP = longPreferencesKey("FIRST_USAGE_TIMESTAMP")
        private val POSTPONED_TIMESTAMP = longPreferencesKey("POSTPONED_TIMESTAMP")
        private val APP_USAGE_POINTS = intPreferencesKey("APP_USAGE_POINTS")
    }
}
