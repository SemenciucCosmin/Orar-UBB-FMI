@file:Suppress("MagicNumber")

package com.ubb.fmi.orar.domain.colorpack.model

/**
 * Every ui element whose color can be customized by the user.
 *
 * @param id Stable identifier, used for persistence and navigation.
 * @param group The [ColorGroup] this item belongs to.
 * @param defaultColor The ARGB color used when the user did not customize this item.
 */
enum class ColorItem(
    val id: String,
    val group: ColorGroup,
    val defaultColor: Long,
) {
    STATUS_ONGOING(
        id = "status_ongoing",
        group = ColorGroup.STATUS_INDICATOR,
        defaultColor = 0xFF1DB954,
    ),
    STATUS_UPCOMING(
        id = "status_upcoming",
        group = ColorGroup.STATUS_INDICATOR,
        defaultColor = 0xFFFFAC1C,
    ),
    EVENT_LECTURE(
        id = "event_lecture",
        group = ColorGroup.EVENT_TYPE,
        defaultColor = 0xFF16A34A,
    ),
    EVENT_SEMINARY(
        id = "event_seminary",
        group = ColorGroup.EVENT_TYPE,
        defaultColor = 0xFF7C3AED,
    ),
    EVENT_LABORATORY(
        id = "event_laboratory",
        group = ColorGroup.EVENT_TYPE,
        defaultColor = 0xFF2563EB,
    ),
    EVENT_STAFF(
        id = "event_staff",
        group = ColorGroup.EVENT_TYPE,
        defaultColor = 0xFFDC2626,
    ),
    EVENT_PERSONAL(
        id = "event_personal",
        group = ColorGroup.EVENT_TYPE,
        defaultColor = 0xFFD97706,
    );

    companion object {
        fun getById(id: String): ColorItem? = entries.firstOrNull { it.id == id }
    }
}
