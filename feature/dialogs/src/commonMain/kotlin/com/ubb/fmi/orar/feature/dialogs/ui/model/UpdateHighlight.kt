package com.ubb.fmi.orar.feature.dialogs.ui.model

import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_calendar
import orar_ubb_fmi.ui.catalog.generated.resources.ic_edit
import orar_ubb_fmi.ui.catalog.generated.resources.ic_explore
import orar_ubb_fmi.ui.catalog.generated.resources.ic_notification_on
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_app_update_announcement_calendar_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_app_update_announcement_calendar_title
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_app_update_announcement_colors_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_app_update_announcement_colors_title
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_app_update_announcement_live_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_app_update_announcement_live_title
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_app_update_announcement_notifications_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_app_update_announcement_notifications_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * Highlight of the update, displayed as a row in the update announcement
 */
enum class UpdateHighlight(
    val iconRes: DrawableResource,
    val titleRes: StringResource,
    val messageRes: StringResource,
) {
    NOTIFICATIONS(
        iconRes = Res.drawable.ic_notification_on,
        titleRes = Res.string.lbl_app_update_announcement_notifications_title,
        messageRes = Res.string.lbl_app_update_announcement_notifications_message,
    ),
    CALENDAR(
        iconRes = Res.drawable.ic_calendar,
        titleRes = Res.string.lbl_app_update_announcement_calendar_title,
        messageRes = Res.string.lbl_app_update_announcement_calendar_message,
    ),
    COLORS(
        iconRes = Res.drawable.ic_edit,
        titleRes = Res.string.lbl_app_update_announcement_colors_title,
        messageRes = Res.string.lbl_app_update_announcement_colors_message,
    ),
    LIVE_TIMETABLE(
        iconRes = Res.drawable.ic_explore,
        titleRes = Res.string.lbl_app_update_announcement_live_title,
        messageRes = Res.string.lbl_app_update_announcement_live_message,
    ),
}
