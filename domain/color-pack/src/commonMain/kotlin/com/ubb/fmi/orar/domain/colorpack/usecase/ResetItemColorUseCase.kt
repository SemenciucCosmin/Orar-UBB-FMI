package com.ubb.fmi.orar.domain.colorpack.usecase

import com.ubb.fmi.orar.data.settings.preferences.SettingsPreferences
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem

/**
 * Use case for restoring the default color of a [ColorItem].
 */
class ResetItemColorUseCase(private val settingsPreferences: SettingsPreferences) {

    suspend operator fun invoke(item: ColorItem) {
        settingsPreferences.removeCustomColor(itemId = item.id)
    }
}
