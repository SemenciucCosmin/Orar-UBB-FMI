package com.ubb.fmi.orar.domain.colorpack.model

/**
 * The full set of ARGB colors for every [ColorItem], combining user customizations with defaults.
 *
 * @param customColors ARGB colors explicitly chosen by the user.
 */
data class ColorPack(
    val customColors: Map<ColorItem, Long> = emptyMap(),
) {

    /**
     * Returns the ARGB color to display for [item]: the custom one if set, otherwise its default.
     */
    operator fun get(item: ColorItem): Long = customColors[item] ?: item.defaultColor

    /**
     * Whether [item] has a user chosen color instead of its default.
     */
    fun isCustomized(item: ColorItem): Boolean = item in customColors

    /**
     * Returns a copy of this pack with [item] set to the ARGB [color].
     */
    fun with(item: ColorItem, color: Long): ColorPack = copy(customColors = customColors + (item to color))

    companion object {
        val Default = ColorPack()
    }
}
