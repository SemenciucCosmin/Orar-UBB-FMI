package com.ubb.fmi.orar.feature.freerooms.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemClickable
import com.ubb.fmi.orar.ui.catalog.components.state.ProgressOverlay
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.ImmutableList
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_location
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_no_results
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Composable function that displays a list of rooms with states.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreeRoomsList(
    rooms: ImmutableList<Owner.Room>,
    isLoading: Boolean,
    onRoomClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        isLoading -> ProgressOverlay(modifier = modifier.fillMaxSize())

        rooms.isEmpty() -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .padding(Pds.spacing.Medium)
                    .fillMaxSize()
            ) {
                Text(
                    text = stringResource(Res.string.lbl_no_results),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(Pds.spacing.Medium),
                verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
            ) {
                items(rooms) { room ->
                    ListItemClickable(
                        headline = room.name,
                        underLine = room.address,
                        onClick = { onRoomClick(room.id) },
                        leadingIcon = painterResource(Res.drawable.ic_location),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
