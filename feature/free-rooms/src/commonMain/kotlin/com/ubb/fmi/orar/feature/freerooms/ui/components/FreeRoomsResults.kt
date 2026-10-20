package com.ubb.fmi.orar.feature.freerooms.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.feature.freerooms.ui.viewmodel.model.FreeRoomsUiState
import com.ubb.fmi.orar.feature.freerooms.ui.viewmodel.model.FreeRoomsUiState.Companion.filteredRooms
import com.ubb.fmi.orar.feature.freerooms.ui.viewmodel.model.FreeRoomsUiState.Companion.isTimeIntervalValid
import com.ubb.fmi.orar.ui.catalog.components.skeleton.ListSkeleton
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_calendar
import orar_ubb_fmi.ui.catalog.generated.resources.ic_search
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_free_rooms_empty
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_free_rooms_empty_details
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_free_rooms_invalid_interval
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_free_rooms_select_day
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_free_rooms_select_day_details
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_time_interval_error
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val SKELETON_ITEMS_COUNT = 6

/**
 * What the results area displays, used to crossfade between them
 */
private enum class FreeRoomsContent {
    LOADING,
    NO_DAY_SELECTED,
    INVALID_INTERVAL,
    EMPTY,
    ROOMS
}

/**
 * Free rooms matching the selection, or a placeholder while loading, or a message explaining why
 * there are none.
 */
@Composable
fun FreeRoomsResults(
    uiState: FreeRoomsUiState,
    onRoomClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val content = when {
        uiState.isLoading -> FreeRoomsContent.LOADING
        uiState.selectedDays.isEmpty() -> FreeRoomsContent.NO_DAY_SELECTED
        !uiState.isTimeIntervalValid -> FreeRoomsContent.INVALID_INTERVAL
        uiState.filteredRooms.isEmpty() -> FreeRoomsContent.EMPTY
        else -> FreeRoomsContent.ROOMS
    }

    Crossfade(
        targetState = content,
        label = "FreeRoomsResults",
        modifier = modifier
    ) { targetContent ->
        when (targetContent) {
            FreeRoomsContent.LOADING -> ListSkeleton(
                itemsCount = SKELETON_ITEMS_COUNT,
                hasLeadingIcon = true,
                hasUnderline = true,
            )

            FreeRoomsContent.NO_DAY_SELECTED -> FreeRoomsMessage(
                icon = painterResource(Res.drawable.ic_calendar),
                title = stringResource(Res.string.lbl_free_rooms_select_day),
                details = stringResource(Res.string.lbl_free_rooms_select_day_details),
            )

            FreeRoomsContent.INVALID_INTERVAL -> FreeRoomsMessage(
                icon = painterResource(Res.drawable.ic_calendar),
                title = stringResource(Res.string.lbl_free_rooms_invalid_interval),
                details = stringResource(Res.string.lbl_time_interval_error),
            )

            FreeRoomsContent.EMPTY -> FreeRoomsMessage(
                icon = painterResource(Res.drawable.ic_search),
                title = stringResource(Res.string.lbl_free_rooms_empty),
                details = stringResource(Res.string.lbl_free_rooms_empty_details),
            )

            FreeRoomsContent.ROOMS -> FreeRoomsList(
                rooms = uiState.filteredRooms,
                onRoomClick = onRoomClick,
            )
        }
    }
}
