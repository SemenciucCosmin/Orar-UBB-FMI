package com.ubb.fmi.orar.feature.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ubb.fmi.orar.feature.app.viewmodel.AppViewModel
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay
import com.ubb.fmi.orar.ui.navigation.components.TimetableBottomBar
import com.ubb.fmi.orar.ui.navigation.graph.ConfigurationFormGraph
import com.ubb.fmi.orar.ui.navigation.graph.TimetableGraph
import com.ubb.fmi.orar.ui.navigation.model.TimetableBottomBarItem
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppRoute() {
    val navController = rememberNavController()
    val viewModel = koinViewModel<AppViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val bottomNavDestinations = TimetableBottomBarItem.entries.map { it.destination }
    val shouldShowBottomBar = bottomNavDestinations.any {
        currentDestination?.hasRoute(it::class) == true
    }

    OrarUbbFmiTheme {
        when (uiState.isConfigurationDone) {
            true -> {
                Scaffold(
                    bottomBar = {
                        AnimatedVisibility(
                            visible = shouldShowBottomBar,
                            enter = expandVertically(),
                            exit = shrinkVertically(),
                            content = { TimetableBottomBar(navController) }
                        )
                    }
                ) { paddingValues ->
                    TimetableGraph(
                        navController = navController,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }

            false -> {
                ConfigurationFormGraph(navController = navController)
            }

            else -> {
                ProgressOverlay(modifier = Modifier.fillMaxSize())
            }
        }
    }
}