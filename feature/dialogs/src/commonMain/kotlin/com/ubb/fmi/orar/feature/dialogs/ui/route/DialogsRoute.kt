package com.ubb.fmi.orar.feature.dialogs.ui.route

import androidx.compose.runtime.Composable
import com.ubb.fmi.orar.feature.dialogs.ui.viewmodel.DialogsViewModel
import com.ubb.fmi.orar.feature.dialogs.ui.viewmodel.model.DialogsUiEvent
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import org.koin.compose.viewmodel.koinViewModel

/**
 * Route for managing dialog events
 */
@Composable
fun DialogsRoute() {
    val viewModel: DialogsViewModel = koinViewModel()

    EventHandler(viewModel.events) { event ->
        when (event) {
            DialogsUiEvent.FEEDBACK_LOOP -> {

            }
        }
    }
}