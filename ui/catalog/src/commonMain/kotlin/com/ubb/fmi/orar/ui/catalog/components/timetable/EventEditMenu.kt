package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun EventEditMenu(
    isVisible: Boolean,
    isNotificationOn: Boolean,
    onVisibleClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier,
    isRemovable: Boolean = false,
    onRemoveClick: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EventVisibilityToggleButton(
            isChecked = isVisible,
            onClick = onVisibleClick
        )

        EventNotificationToggleButton(
            isChecked = isNotificationOn,
            onClick = onNotificationClick
        )

        if (isRemovable) {
            EventRemoveButton(onRemove = onRemoveClick)
        }
    }
}