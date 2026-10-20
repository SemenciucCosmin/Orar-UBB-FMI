package com.ubb.fmi.orar.ui.navigation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ubb.fmi.orar.ui.navigation.destination.MainNavDestination
import com.ubb.fmi.orar.ui.navigation.model.BottomBarItem
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Composable function that displays the floating bottom navigation bar for the timetable.
 *
 * @param navController The NavController used for navigation.
 */
@Composable
fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = CircleShape,
        shadowElevation = Pds.spacing.Small,
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = Pds.spacing.Large, vertical = Pds.spacing.Small)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .selectableGroup()
                .padding(Pds.spacing.Small)
        ) {
            BottomBarItem.entries.forEach { bottomBarItem ->
                val destination = bottomBarItem.destination

                BottomBarTab(
                    icon = painterResource(bottomBarItem.icon),
                    label = stringResource(bottomBarItem.labelRes),
                    isSelected = currentDestination?.hasRoute(destination::class) == true,
                    onClick = {
                        navController.navigate(destination) {
                            popUpTo(MainNavDestination.UserMain()) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewTimetableBottomBar() {
    OrarUbbFmiTheme {
        BottomBar(
            navController = rememberNavController()
        )
    }
}
