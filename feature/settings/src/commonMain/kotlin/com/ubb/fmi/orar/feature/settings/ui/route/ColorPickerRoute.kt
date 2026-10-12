package com.ubb.fmi.orar.feature.settings.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.settings.ui.components.ColorPickerScreen
import com.ubb.fmi.orar.feature.settings.viewmodel.ColorPickerViewModel
import com.ubb.fmi.orar.feature.settings.viewmodel.model.ColorPickerUiState.ColorPickerUiEvent
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Route for picking the color of the color item with [colorItemId]
 */
@Composable
fun ColorPickerRoute(
    navController: NavController,
    colorItemId: String,
) {
    val viewModel: ColorPickerViewModel = koinViewModel(
        parameters = { parametersOf(colorItemId) }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    EventHandler(viewModel.events) { event ->
        LaunchedEffect(event) {
            viewModel.unregisterEvent(event)
            when (event) {
                ColorPickerUiEvent.SAVED,
                ColorPickerUiEvent.INVALID_ITEM -> navController.navigateUp()
            }
        }
    }

    ColorPickerScreen(
        uiState = uiState,
        onColorChange = viewModel::selectColor,
        onSaveClick = viewModel::save,
        onBack = navController::navigateUp
    )
}
