package com.ubb.fmi.orar.domain.colorpack.usecase

import com.ubb.fmi.orar.data.settings.preferences.SettingsPreferences
import com.ubb.fmi.orar.domain.colorpack.extensions.hexToArgbOrNull
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem
import com.ubb.fmi.orar.domain.colorpack.model.ColorPack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use case for observing the [ColorPack] built from the user customized colors.
 */
class GetColorPackUseCase(private val settingsPreferences: SettingsPreferences) {

    operator fun invoke(): Flow<ColorPack> {
        return settingsPreferences.getCustomColors().map { customColors ->
            ColorPack(
                customColors = customColors.mapNotNull { (itemId, hex) ->
                    val item = ColorItem.getById(itemId) ?: return@mapNotNull null
                    val color = hex.hexToArgbOrNull() ?: return@mapNotNull null
                    item to color
                }.toMap()
            )
        }
    }
}
