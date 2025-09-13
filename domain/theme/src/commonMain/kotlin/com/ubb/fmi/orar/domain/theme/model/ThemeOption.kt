package com.ubb.fmi.orar.domain.theme.model

/**
 * Enum class for defining all theme options
 */
enum class ThemeOption(val id: String) {
    LIGHT(id = "light"),
    DARK(id = "dark"),
    SYSTEM(id = "system");

    companion object {
        fun getById(id: String): ThemeOption {
            return ThemeOption.entries.firstOrNull { it.id == id } ?: SYSTEM
        }
    }
}
