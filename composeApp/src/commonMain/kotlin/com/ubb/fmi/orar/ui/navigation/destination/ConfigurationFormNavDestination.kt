package com.ubb.fmi.orar.ui.navigation.destination

import kotlinx.serialization.Serializable

@Serializable
sealed class ConfigurationFormNavDestination {

    @Serializable
    data object OnboardingForm: ConfigurationFormNavDestination()

    @Serializable
    data object TeachersForm: ConfigurationFormNavDestination()

    @Serializable
    data object StudyLinesForm: ConfigurationFormNavDestination()

    @Serializable
    data object StudyGroupsForm: ConfigurationFormNavDestination()
}
