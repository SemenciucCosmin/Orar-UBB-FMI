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
    data class StudyGroupsForm(
        val year: Int,
        val semesterId: String,
        val studyLineBaseId: String,
        val studyLineYearId: String,
        val studyLineDegreeId: String,
    ): ConfigurationFormNavDestination()
}
