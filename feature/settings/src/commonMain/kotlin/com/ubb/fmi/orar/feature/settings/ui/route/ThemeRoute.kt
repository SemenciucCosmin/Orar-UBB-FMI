package com.ubb.fmi.orar.feature.settings.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.settings.ui.components.ThemeScreen
import com.ubb.fmi.orar.feature.settings.viewmodel.ThemeViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Route for theme setting selection
 */
@Composable
fun ThemeRoute(navController: NavController) {
    val viewModel: ThemeViewModel = koinViewModel()
    val themeOption by viewModel.themeOption.collectAsStateWithLifecycle()

    ThemeScreen(
        selectedThemeOption = themeOption,
        onThemeOptionClick = viewModel::selectThemeOption,
        onBack = navController::navigateUp
    )
}
