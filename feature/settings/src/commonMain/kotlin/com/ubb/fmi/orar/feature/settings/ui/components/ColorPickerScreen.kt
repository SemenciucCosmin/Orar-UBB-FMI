package com.ubb.fmi.orar.feature.settings.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.domain.colorpack.model.ColorGroup
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem
import com.ubb.fmi.orar.feature.settings.viewmodel.model.ColorPickerUiState
import com.ubb.fmi.orar.ui.catalog.components.PrimaryButton
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.calendar.CalendarDayBox
import com.ubb.fmi.orar.ui.catalog.components.timetable.EventTypeChip
import com.ubb.fmi.orar.ui.catalog.components.timetable.StatusIndicator
import com.ubb.fmi.orar.ui.catalog.extensions.colorItem
import com.ubb.fmi.orar.ui.catalog.extensions.labelRes
import com.ubb.fmi.orar.ui.colorpack.ColorPackProvider
import com.ubb.fmi.orar.ui.colorpack.components.ColorPicker
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_hex_code
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_invalid_hex_code
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_preview
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_save
import org.jetbrains.compose.resources.stringResource

private const val PREVIEW_DAY_OF_MONTH = 15

/**
 * Screen for picking the color of a single [ColorItem], with a live preview and a save action.
 */
@Composable
fun ColorPickerScreen(
    uiState: ColorPickerUiState,
    onColorChange: (Color) -> Unit,
    onSaveClick: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(uiState.item.labelRes),
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(Pds.spacing.Medium)
        ) {
            ColorPackProvider(colorPack = uiState.previewColorPack) {
                ColorPreview(item = uiState.item)
            }

            ColorPicker(
                color = uiState.selectedColor,
                onColorChange = onColorChange,
                hexLabel = stringResource(Res.string.lbl_hex_code),
                invalidHexMessage = stringResource(Res.string.lbl_invalid_hex_code),
            )

            PrimaryButton(
                text = stringResource(Res.string.lbl_save),
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun ColorPreview(item: ColorItem) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.SMedium),
            modifier = Modifier.padding(Pds.spacing.Medium)
        ) {
            Text(
                text = stringResource(Res.string.lbl_preview),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                when (item.group) {
                    ColorGroup.STATUS_INDICATOR -> StatusIndicator(
                        defaults = when (item) {
                            ColorItem.STATUS_UPCOMING -> StatusIndicator.Upcoming()
                            else -> StatusIndicator.Ongoing()
                        },
                        textStyle = MaterialTheme.typography.titleMedium,
                        dotSize = Pds.spacing.Small,
                    )

                    ColorGroup.EVENT_TYPE ->
                        EventType.entries
                            .firstOrNull { it.colorItem == item }
                            ?.let { type ->
                                EventTypeChip(text = "", type = type, enabled = true)
                            }

                    ColorGroup.ACADEMIC_CALENDAR -> CalendarDayBox(
                        dayOfMonth = PREVIEW_DAY_OF_MONTH,
                        colorItem = item,
                        isToday = false,
                        isCurrentMonth = true,
                        modifier = Modifier.size(Pds.icon.Large),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewColorPickerScreen() {
    OrarUbbFmiTheme {
        ColorPickerScreen(
            uiState = ColorPickerUiState(item = ColorItem.EVENT_LECTURE),
            onColorChange = {},
            onSaveClick = {},
            onBack = {},
        )
    }
}
