package com.ubb.fmi.orar.ui.navigation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import com.ubb.fmi.orar.ui.navigation.model.TimetableBottomBarItem
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable function that displays the bottom navigation bar for the timetable.
 *
 * @param navController The NavController used for navigation.
 */
@Composable
fun TimetableBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar {
        TimetableBottomBarItem.entries.forEach { timetableBottomBarItem ->
            val destination = timetableBottomBarItem.destination
            val isSelected = currentDestination?.hasRoute(destination::class) == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(timetableBottomBarItem.destination) {
                        popUpTo(TimetableNavDestination.UserTimetable) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        modifier = Modifier.size(Pds.icon.SMedium),
                        painter = painterResource(timetableBottomBarItem.icon),
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = stringResource(timetableBottomBarItem.labelRes))
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewTimetableBottomBar() {
    OrarUbbFmiTheme {
        TimetableBottomBar(
            navController = rememberNavController()
        )
    }
}
