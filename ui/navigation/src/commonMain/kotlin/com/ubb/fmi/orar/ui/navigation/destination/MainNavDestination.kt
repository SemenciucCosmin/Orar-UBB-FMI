package com.ubb.fmi.orar.ui.navigation.destination

import kotlinx.serialization.Serializable

/**
 * Represents the navigation destinations for the timetable feature in the application.
 *
 * This sealed class defines various destinations related to timetables, such as user timetables,
 * study lines, groups, teachers, subjects, and rooms. Each destination is represented by a data
 * class with relevant parameters.
 */
@Serializable
sealed class MainNavDestination {

    /**
     * Represents a navigation destination for the timetable feature.
     *
     * This sealed class is used to define different types of destinations that can be navigated to
     * within the timetable feature. Each subclass represents a specific destination with its own parameters.
     */
    @Serializable
    data object Startup : MainNavDestination()

    /**
     * Represents a user-specific timetable destination.
     * This destination is used to navigate to the timetable for a specific user.
     */
    @Serializable
    data object UserMain : MainNavDestination()

    /**
     * Represents the news destination.
     */
    @Serializable
    data object News : MainNavDestination()

    /**
     * Represents the explore destination.
     * Navigation hub with options for other destinations
     */
    @Serializable
    data object Explore : MainNavDestination()
}
