package com.ubb.fmi.orar.feature.feedback.ui.navigation

import com.ubb.fmi.orar.feature.feedback.ui.model.FeedbackChoice
import kotlinx.serialization.Serializable

/**
 * All feedback nav destinations
 */
@Serializable
sealed class FeedbackNavDestination {

    @Serializable
    data object Choice : FeedbackNavDestination()

    @Serializable
    data class Outcome(val feedbackChoice: FeedbackChoice) : FeedbackNavDestination()
}
