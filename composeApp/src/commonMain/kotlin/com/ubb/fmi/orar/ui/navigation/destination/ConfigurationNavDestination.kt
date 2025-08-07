package com.ubb.fmi.orar.ui.navigation.destination

import kotlinx.serialization.Serializable

@Serializable
sealed class ConfigurationNavDestination {

    @Serializable
    data class OnboardingForm(val title: String): ConfigurationNavDestination()

    @Serializable
    data class TeachersForm(val teacherTitleId: String): ConfigurationNavDestination()

    @Serializable
    data object StudyLinesForm: ConfigurationNavDestination()

    @Serializable
    data object StudyGroupsForm: ConfigurationNavDestination()
}
