package com.ubb.fmi.orar.feature.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.ubb.fmi.orar.feature.app.viewmodel.AppViewModel
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay
import com.ubb.fmi.orar.ui.navigation.graph.ConfigurationFormGraph
import com.ubb.fmi.orar.ui.navigation.graph.TimetableGraph
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppRoute() {
    val navController = rememberNavController()
    val viewModel = koinViewModel<AppViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OrarUbbFmiTheme {
        when (uiState.isConfigurationDone) {
            true -> TimetableGraph(navController = navController)
            false -> ConfigurationFormGraph(navController = navController)
            else -> ProgressOverlay(modifier = Modifier.fillMaxSize())
        }
    }
}