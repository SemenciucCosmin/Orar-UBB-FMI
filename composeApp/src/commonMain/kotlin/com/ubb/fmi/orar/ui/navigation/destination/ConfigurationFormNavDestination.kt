package com.ubb.fmi.orar.ui.navigation.destination

import kotlinx.serialization.Serializable

@Serializable
sealed class ConfigurationFormNavDestination {

    @Serializable
    data object OnboardingForm: ConfigurationFormNavDestination()

    @Serializable
    data class TeachersForm(
        val year: Int,
        val semesterId: String,
    ): ConfigurationFormNavDestination()

    @Serializable
    data class StudyLinesForm(
        val year: Int,
        val semesterId: String,
    ): ConfigurationFormNavDestination()

    @Serializable
    data class GroupsForm(
        val year: Int,
        val semesterId: String,
        val fieldId: String,
        val studyLevelId: String,
        val studyLineDegreeId: String,
    ): ConfigurationFormNavDestination()
}
