package com.ubb.fmi.orar.feature.settings.ui.model

import com.ubb.fmi.orar.domain.theme.model.ThemeOption
import orar_ubb_fmi.feature.settings.generated.resources.Res
import orar_ubb_fmi.feature.settings.generated.resources.lbl_dark
import orar_ubb_fmi.feature.settings.generated.resources.lbl_light
import orar_ubb_fmi.feature.settings.generated.resources.lbl_system
import org.jetbrains.compose.resources.StringResource

/**
 * Extension property of [ThemeOption] for ui labeling
 */
val ThemeOption.labelRes: StringResource
    get() {
        return when (this) {
            ThemeOption.LIGHT -> Res.string.lbl_light
            ThemeOption.DARK -> Res.string.lbl_dark
            ThemeOption.SYSTEM -> Res.string.lbl_system
        }
    }
