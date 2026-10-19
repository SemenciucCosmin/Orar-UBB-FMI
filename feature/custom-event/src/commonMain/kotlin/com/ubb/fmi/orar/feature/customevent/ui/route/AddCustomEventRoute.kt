package com.ubb.fmi.orar.feature.customevent.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.customevent.ui.components.AddCustomEventScreen
import com.ubb.fmi.orar.feature.customevent.ui.viewmodel.AddCustomEventViewModel
import com.ubb.fmi.orar.feature.customevent.ui.viewmodel.model.AddCustomEventUiState
import com.ubb.fmi.orar.feature.customevent.ui.viewmodel.model.isNextEnabled
import com.ubb.fmi.orar.feature.customevent.ui.viewmodel.model.isTimeIntervalValid
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import com.ubb.fmi.orar.ui.catalog.extensions.getContext
import com.ubb.fmi.orar.ui.catalog.extensions.showToast
import com.ubb.fmi.orar.ui.catalog.model.ToastLength
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_event_added
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddCustomEventRoute(navController: NavController) {
    val context = getContext()
    val viewModel: AddCustomEventViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AddCustomEventScreen(
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
        isTimeIntervalValid = uiState.isTimeIntervalValid,
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
            AddCustomEventUiState.AddCustomEventUiEvent.SUCCESS -> {
                viewModel.unregisterEvent(event)
                showToast(context, stringResource(Res.string.lbl_event_added), ToastLength.SHORT)
                navController.navigateUp()
            }
        }
    }
}