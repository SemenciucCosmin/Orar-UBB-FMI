package com.ubb.fmi.orar.feature.freerooms.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemClickable
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.ImmutableList
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_location
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_free_rooms_count
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource

/**
 * Free rooms preceded by their count
 */
@Composable
fun FreeRoomsList(
    rooms: ImmutableList<Owner.Room>,
    onRoomClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(Pds.spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(Pds.spacing.SMedium),
        modifier = modifier
    ) {
        item {
            Text(
                text = pluralStringResource(Res.plurals.lbl_free_rooms_count, rooms.size, rooms.size),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        items(items = rooms, key = { it.id }) { room ->
            ListItemClickable(
                headline = room.name,
                underLine = room.address,
                onClick = { onRoomClick(room.id) },
                leadingIcon = painterResource(Res.drawable.ic_location),
                modifier = Modifier.animateItem()
            )
        }
    }
}
