package com.ubb.fmi.orar.ui.navigation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ubb.fmi.orar.ui.navigation.model.TimetableBottomBarItem
import org.jetbrains.compose.resources.painterResource

@Composable
fun TimetableBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar {
        TimetableBottomBarItem.entries.forEach { timetableBottomBarItem ->
            NavigationBarItem(
                selected = currentDestination?.hasRoute(
                    timetableBottomBarItem.destination::class
                ) == true,
                onClick = {
                    navController.navigate(timetableBottomBarItem.destination) {
                        navController.graph.findStartDestination().route?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(timetableBottomBarItem.icon),
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = timetableBottomBarItem.label)
                }
            )
        }
    }
}