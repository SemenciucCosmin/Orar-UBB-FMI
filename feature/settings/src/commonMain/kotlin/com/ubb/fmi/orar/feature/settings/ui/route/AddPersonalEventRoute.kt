package com.ubb.fmi.orar.feature.settings.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.settings.ui.components.AddPersonalEventScreen
import com.ubb.fmi.orar.feature.settings.viewmodel.AddPersonalEventViewModel
import com.ubb.fmi.orar.feature.settings.viewmodel.model.AddPersonalEventUiState
import com.ubb.fmi.orar.feature.settings.viewmodel.model.isNextEnabled
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import com.ubb.fmi.orar.ui.catalog.extensions.getContext
import com.ubb.fmi.orar.ui.catalog.extensions.showToast
import com.ubb.fmi.orar.ui.catalog.model.ToastLength
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_event_added
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddPersonalEventRoute(navController: NavController) {
    val context = getContext()
    val viewModel: AddPersonalEventViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AddPersonalEventScreen(
        activity = uiState.activity,
        caption = uiState.caption,
        location = uiState.location,
        details = uiState.details,
        startHour = uiState.startHour,
        startMinute = uiState.startMinute,
        endHour = uiState.endHour,
        endMinute = uiState.endMinute,
        selectedFrequency = uiState.selectedFrequency,
        selectedDays = uiState.selectedDays,
        isNextEnabled = uiState.isNextEnabled,
        onActivityChange = viewModel::changeActivity,
        onCaptionChange = viewModel::changeCaption,
        onLocationChange = viewModel::changeLocation,
        onDetailsChange = viewModel::changeDetails,
        onStartHourChange = viewModel::changeStartHour,
        onStartMinuteChange = viewModel::changeStartMinute,
        onEndHourChange = viewModel::changeEndHour,
        onEndMinuteChange = viewModel::changeEndMinute,
        onSelectFrequency = viewModel::selectFrequency,
        onSelectDays = viewModel::selectDays,
        onNextClick = viewModel::finish,
        onBack = navController::navigateUp
    )

    EventHandler(viewModel.events) { event ->
        when (event) {
            AddPersonalEventUiState.AddPersonalEventUiEvent.SUCCESS -> {
                viewModel.unregisterEvent(event)
                showToast(context, stringResource(Res.string.lbl_event_added), ToastLength.SHORT)
                navController.navigateUp()
            }
        }
    }
}