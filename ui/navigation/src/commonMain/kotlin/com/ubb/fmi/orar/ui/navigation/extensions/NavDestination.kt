package com.ubb.fmi.orar.ui.navigation.extensions

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.ubb.fmi.orar.ui.navigation.model.BottomBarItem

/**
 * Whether the destination is one of the [BottomBarItem] tabs, on which the bottom bar is displayed
 */
fun NavDestination?.isBottomBarDestination(): Boolean {
    return BottomBarItem.entries.any { this?.hasRoute(it.destination::class) == true }
}
