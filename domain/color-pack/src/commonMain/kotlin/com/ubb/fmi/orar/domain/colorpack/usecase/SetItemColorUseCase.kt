package com.ubb.fmi.orar.domain.colorpack.usecase

import com.ubb.fmi.orar.data.settings.preferences.SettingsPreferences
import com.ubb.fmi.orar.domain.colorpack.extensions.argbToHex
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem

/**
 * Use case for saving a user chosen ARGB color for a [ColorItem].
 */
class SetItemColorUseCase(private val settingsPreferences: SettingsPreferences) {

    suspend operator fun invoke(item: ColorItem, color: Long) {
        settingsPreferences.setCustomColor(itemId = item.id, hex = color.argbToHex())
    }
}
