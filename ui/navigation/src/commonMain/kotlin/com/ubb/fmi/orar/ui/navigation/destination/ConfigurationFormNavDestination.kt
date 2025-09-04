package com.ubb.fmi.orar.ui.navigation.destination

import kotlinx.serialization.Serializable

/**
 * Represents the navigation destinations for configuration forms in the application.
 *
 * This sealed class defines various forms related to configuration, such as onboarding,
 * teachers, study lines, and groups. Each form is represented by a data class with relevant
 * parameters.
 */
@Serializable
sealed class ConfigurationFormNavDestination {

    /**
     * Represents a configuration form destination.
     *
     * This sealed class is used to define different types of configuration forms that can be navigated to.
     * Each subclass represents a specific form with its own parameters.
     */
    @Serializable
    data class OnboardingForm(
        val configurationFormTypeId: String
    ) : ConfigurationFormNavDestination()

    /**
     * Represents a form for managing teachers.
     *
     * @property year The academic year for which the form is applicable.
     * @property semesterId The identifier for the semester.
     */
    @Serializable
    data class TeachersForm(
        val year: Int,
        val semesterId: String,
    ) : ConfigurationFormNavDestination()

    /**
     * Represents a form for managing study lines.
     *
     * @property year The academic year for which the form is applicable.
     * @property semesterId The identifier for the semester.
     */
    @Serializable
    data class StudyLinesForm(
        val year: Int,
        val semesterId: String,
    ) : ConfigurationFormNavDestination()

    /**
     * Represents a form for managing groups.
     *
     * @property year The academic year for which the form is applicable.
     * @property semesterId The identifier for the semester.
     * @property fieldId The identifier for the field of study.
     * @property studyLevelId The identifier for the study level.
     * @property studyLineDegreeId The identifier for the study line degree.
     */
    @Serializable
    data class GroupsForm(
        val year: Int,
        val semesterId: String,
        val fieldId: String,
        val studyLevelId: String,
        val studyLineDegreeId: String,
    ) : ConfigurationFormNavDestination()
}
