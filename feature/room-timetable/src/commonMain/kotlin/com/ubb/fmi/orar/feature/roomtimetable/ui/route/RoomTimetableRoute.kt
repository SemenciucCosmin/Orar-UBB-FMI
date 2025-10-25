package com.ubb.fmi.orar.feature.roomtimetable.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.roomtimetable.ui.viewmodel.RoomTimetableViewModel
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.timetable.TimetableFrequencyTab
import com.ubb.fmi.orar.ui.catalog.components.timetable.TimetableScreen
import com.ubb.fmi.orar.ui.catalog.extensions.getContext
import com.ubb.fmi.orar.ui.catalog.extensions.showToast
import com.ubb.fmi.orar.ui.catalog.model.TimetableUiEvent
import com.ubb.fmi.orar.ui.catalog.model.ToastLength
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_event_added
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 *  Composable function that represents the Room Timetable route.
 * @param navController The NavController to handle navigation actions.
 * @param roomId The ID of the room for which the timetable is displayed.
 */
@Composable
fun RoomTimetableRoute(
    navController: NavController,
    roomId: String,
) {
    val context = getContext()
    val viewModel: RoomTimetableViewModel = koinViewModel(parameters = { parametersOf(roomId) })
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TimetableScreen(
        uiState = uiState,
        onRetryClick = viewModel::retry,
        onAddItem = viewModel::addEvent,
        topBar = {
            TopBar(
                title = uiState.title,
                onBack = navController::navigateUp,
                trailingContent = {
                    if (uiState.errorStatus == null) {
                        TimetableFrequencyTab(
                            selectedFrequency = uiState.selectedFrequency,
                            onFrequencyClick = viewModel::selectFrequency
                        )
                    }
                }
            )
        }
    )

    EventHandler(viewModel.events) { event ->
        when (event) {
            TimetableUiEvent.EVENT_ADOPTION_SUCCESSFUL -> {
                viewModel.unregisterEvent(event)
                showToast(context, stringResource(Res.string.lbl_event_added), ToastLength.SHORT)
            }
        }
    }
}
