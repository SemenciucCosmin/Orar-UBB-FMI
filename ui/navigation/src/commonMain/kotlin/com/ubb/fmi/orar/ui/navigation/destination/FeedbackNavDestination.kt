package com.ubb.fmi.orar.ui.navigation.destination

import kotlinx.serialization.Serializable

/**
 * All feedback nav destinations
 */
@Serializable
sealed class FeedbackNavDestination {

    @Serializable
    data object Choice : FeedbackNavDestination()

    @Serializable
    data class Outcome(val feedbackChoiceId: String) : FeedbackNavDestination()
}