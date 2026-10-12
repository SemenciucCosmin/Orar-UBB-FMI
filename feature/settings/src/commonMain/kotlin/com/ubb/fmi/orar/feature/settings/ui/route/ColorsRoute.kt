package com.ubb.fmi.orar.feature.settings.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.settings.ui.components.ColorsScreen
import com.ubb.fmi.orar.feature.settings.viewmodel.ColorsViewModel
import com.ubb.fmi.orar.ui.navigation.destination.SettingsNavDestination
import org.koin.compose.viewmodel.koinViewModel

/**
 * Route for the customizable colors list
 */
@Composable
fun ColorsRoute(navController: NavController) {
    val viewModel: ColorsViewModel = koinViewModel()
    val colorPack by viewModel.colorPack.collectAsStateWithLifecycle()

    ColorsScreen(
        colorPack = colorPack,
        onItemClick = { item ->
            navController.navigate(SettingsNavDestination.ColorPicker(colorItemId = item.id))
        },
        onResetClick = viewModel::resetColor,
        onBack = navController::navigateUp
    )
}
