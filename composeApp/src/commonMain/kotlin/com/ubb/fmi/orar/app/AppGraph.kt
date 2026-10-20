package com.ubb.fmi.orar.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ubb.fmi.orar.feature.feedback.ui.navigation.feedbackGraph
import com.ubb.fmi.orar.ui.catalog.LocalBottomBarPadding
import com.ubb.fmi.orar.ui.navigation.components.BottomBar
import com.ubb.fmi.orar.ui.navigation.destination.MainNavDestination
import com.ubb.fmi.orar.ui.navigation.extensions.isBottomBarDestination

private const val FADE_MILLIS = 450

/**
 * Main navigation graph for the Orar UBB FMI application.
 * This function sets up the navigation host and defines the start destination.
 * It includes the configuration form and timetable graphs.
 *
 * The bottom bar floats above the tab destinations as a single instance, so it animates between
 * tabs instead of being recreated by each of them. Its height is provided through
 * [LocalBottomBarPadding], for the content to scroll behind it.
 */
@Composable
fun AppGraph(navController: NavHostController) {
    val density = LocalDensity.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isBottomBarVisible = navBackStackEntry?.destination.isBottomBarDestination()
    var bottomBarHeight by remember { mutableStateOf(0.dp) }

    Box(modifier = Modifier.fillMaxSize()) {
        CompositionLocalProvider(
            LocalBottomBarPadding provides if (isBottomBarVisible) bottomBarHeight else 0.dp
        ) {
            NavHost(
                navController = navController,
                startDestination = MainNavDestination.Startup,
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                enterTransition = { fadeIn(tween(FADE_MILLIS)) },
                exitTransition = { fadeOut(tween(FADE_MILLIS)) },
                popEnterTransition = { fadeIn(tween(FADE_MILLIS)) },
                popExitTransition = { fadeOut(tween(FADE_MILLIS)) },
            ) {
                configurationFormGraph(navController)
                mainGraph(navController)
                exploreGraph(navController)
                settingsGraph(navController)
                feedbackGraph(navController)
            }
        }

        AnimatedVisibility(
            visible = isBottomBarVisible,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomBar(
                navController = navController,
                modifier = Modifier.onSizeChanged {
                    bottomBarHeight = with(density) { it.height.toDp() }
                }
            )
        }
    }
}
