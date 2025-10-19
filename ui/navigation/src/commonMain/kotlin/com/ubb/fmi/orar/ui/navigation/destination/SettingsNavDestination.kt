package com.ubb.fmi.orar.ui.navigation.destination

import kotlinx.serialization.Serializable

/**
 * Represents the navigation destinations for the settings feature in the application.
 * This sealed class defines various destinations related to settings.
 */
@Serializable
sealed class SettingsNavDestination {

    /**
     * Represents a navigation destination for the settings main screen
     */
    @Serializable
    data object Settings : SettingsNavDestination()

    /**
     * Represents a navigation destination for the theme feature.
     */
    @Serializable
    data object Theme : SettingsNavDestination()

    /**
     * Represents a navigation destination for the personal event addition feature.
     */
    @Serializable
    data object AddPersonalEvent : SettingsNavDestination()
}
