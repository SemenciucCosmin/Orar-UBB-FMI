package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.domain.extensions.formatTime
import com.ubb.fmi.orar.ui.catalog.components.animation.rememberAnimatedCardState
import com.ubb.fmi.orar.ui.catalog.extensions.labelRes
import com.ubb.fmi.orar.ui.catalog.model.TimetableListItem
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource

@Composable
fun EventsList(
    items: ImmutableList<TimetableListItem>,
    isEditModeOn: Boolean,
    listState: LazyListState,
    onVisibleClick: (TimetableListItem.Event) -> Unit,
    onNotificationClick: (TimetableListItem.Event) -> Unit,
    onRemoveClick: (TimetableListItem.Event) -> Unit,
    modifier: Modifier = Modifier,
    selectedEventId: String? = null,
    onAddItem: ((String) -> Unit)? = null,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
        contentPadding = PaddingValues(Pds.spacing.SMedium),
        state = listState,
    ) {
        items(
            items = items,
            key = { timetableItem ->
                when (timetableItem) {
                    is TimetableListItem.Divider -> timetableItem.day
                    is TimetableListItem.Event -> timetableItem.id
                }
            }
        ) { timetableItem ->
            when (timetableItem) {
                is TimetableListItem.Divider -> {
                    TimetableListDivider(
                        modifier = Modifier.animateItem(),
                        text = stringResource(timetableItem.day.labelRes),
                    )
                }

                is TimetableListItem.Event -> {
                    val animatedCardState = rememberAnimatedCardState()

                    LaunchedEffect(selectedEventId) {
                        if (timetableItem.id == selectedEventId) {
                            animatedCardState.animateShake()
                        }
                    }

                    Row(
                        modifier = Modifier.animateItem(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Pds.spacing.SMedium)
                    ) {
                        AnimatedVisibility(isEditModeOn) {
                            EventEditMenu(
                                isVisible = timetableItem.isVisible,
                                isRemovable = timetableItem.isPersonal,
                                isNotificationOn = timetableItem.isNotificationOn,
                                onVisibleClick = { onVisibleClick(timetableItem) },
                                onNotificationClick = { onNotificationClick(timetableItem) },
                                onRemoveClick = { onRemoveClick(timetableItem) }
                            )
                        }

                        EventCard(
                            startTime = formatTime(
                                timetableItem.startHour,
                                timetableItem.startMinute
                            ),
                            endTime = formatTime(
                                timetableItem.endHour,
                                timetableItem.endMinute
                            ),
                            enabled = timetableItem.isVisible,
                            expanded = !isEditModeOn,
                            location = timetableItem.location,
                            title = timetableItem.title,
                            type = timetableItem.type,
                            participant = timetableItem.participant,
                            caption = timetableItem.caption,
                            details = timetableItem.details,
                            animatedCardState = animatedCardState,
                            onAddClick = onAddItem?.let {
                                {
                                    onAddItem(timetableItem.id)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}