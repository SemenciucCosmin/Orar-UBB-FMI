package com.ubb.fmi.orar.feature.startup.ui.viewmodel.model

import com.ubb.fmi.orar.ui.catalog.viewmodel.model.Event

/**
 * Events that can be emitted during the startup process.
 */
enum class StartupEvent : Event {
    CONFIGURATION_COMPLETE,
    CONFIGURATION_INCOMPLETE,
}
