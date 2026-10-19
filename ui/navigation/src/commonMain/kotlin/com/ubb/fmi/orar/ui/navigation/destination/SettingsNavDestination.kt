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
     * Represents a navigation destination for the notifications settings feature.
     */
    @Serializable
    data object Notifications : SettingsNavDestination()

    /**
     * Represents a navigation destination for the customizable colors list.
     */
    @Serializable
    data object Colors : SettingsNavDestination()

    /**
     * Represents a navigation destination for picking the color of a single color item.
     * @param colorItemId The id of the color item being customized.
     */
    @Serializable
    data class ColorPicker(val colorItemId: String) : SettingsNavDestination()

    /**
     * Represents a navigation destination for the about screen, with app details and support links.
     */
    @Serializable
    data object About : SettingsNavDestination()
}
