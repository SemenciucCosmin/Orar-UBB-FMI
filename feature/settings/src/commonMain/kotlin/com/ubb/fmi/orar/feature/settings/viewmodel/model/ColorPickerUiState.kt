package com.ubb.fmi.orar.feature.settings.viewmodel.model

import androidx.compose.ui.graphics.Color
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem
import com.ubb.fmi.orar.domain.colorpack.model.ColorPack
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.UiEvent
import com.ubb.fmi.orar.ui.colorpack.extensions.getColor
import com.ubb.fmi.orar.ui.colorpack.extensions.with

/**
 * Ui state of the color picker screen.
 *
 * @param item The color item being customized.
 * @param colorPack The currently saved colors.
 * @param selectedColor The unsaved color currently chosen in the picker.
 */
data class ColorPickerUiState(
    val item: ColorItem,
    val colorPack: ColorPack = ColorPack.Default,
    val selectedColor: Color = colorPack.getColor(item),
) {

    /**
     * The saved colors with the unsaved selection applied, used for the live preview.
     */
    val previewColorPack: ColorPack
        get() = colorPack.with(item, selectedColor)

    /**
     * Ui events emitted by the color picker screen.
     */
    enum class ColorPickerUiEvent : UiEvent {
        SAVED,
        INVALID_ITEM,
    }
}
