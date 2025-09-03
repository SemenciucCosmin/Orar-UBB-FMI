package com.ubb.fmi.orar.ui.catalog.model

enum class ConfigurationFormType(val id: String) {
    STARTUP(id = "startup"),
    SETTINGS(id = "settings");

    companion object {
        fun getById(id: String): ConfigurationFormType {
            return entries.firstOrNull { it.id == id } ?: STARTUP
        }
    }
}