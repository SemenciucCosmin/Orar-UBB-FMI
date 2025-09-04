package com.ubb.fmi.orar.ui.catalog.model

/**
 * Represents the type of configuration form in the application.
 *
 * @property id The unique identifier for the configuration form type.
 */
enum class ConfigurationFormType(val id: String) {
    STARTUP(id = "startup"),
    SETTINGS(id = "settings");

    companion object {
        fun getById(id: String): ConfigurationFormType {
            return entries.firstOrNull { it.id == id } ?: STARTUP
        }
    }
}