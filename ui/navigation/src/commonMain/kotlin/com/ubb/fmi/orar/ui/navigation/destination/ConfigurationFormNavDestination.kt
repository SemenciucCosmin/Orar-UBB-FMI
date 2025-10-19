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
     */
    @Serializable
    data object TeachersForm : ConfigurationFormNavDestination()

    /**
     * Represents a form for managing study lines.
     */
    @Serializable
    data object StudyLinesForm : ConfigurationFormNavDestination()

    /**
     * Represents a form for managing groups.
     */
    @Serializable
    data object GroupsForm : ConfigurationFormNavDestination()
}
