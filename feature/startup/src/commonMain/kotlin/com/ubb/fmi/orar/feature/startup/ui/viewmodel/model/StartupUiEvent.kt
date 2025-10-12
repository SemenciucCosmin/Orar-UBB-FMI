package com.ubb.fmi.orar.feature.startup.ui.viewmodel.model

import com.ubb.fmi.orar.ui.catalog.viewmodel.model.UiEvent

/**
 * Events that can be emitted during the startup process.
 */
enum class StartupUiEvent : UiEvent {
    CONFIGURATION_COMPLETE,
    CONFIGURATION_INCOMPLETE,
}
