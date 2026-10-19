package com.ubb.fmi.orar.feature.settings.ui.model

import com.ubb.fmi.orar.ui.catalog.extensions.getStoreAppUrl
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_building
import orar_ubb_fmi.ui.catalog.generated.resources.ic_custom
import orar_ubb_fmi.ui.catalog.generated.resources.ic_explore
import orar_ubb_fmi.ui.catalog.generated.resources.ic_face_negative
import orar_ubb_fmi.ui.catalog.generated.resources.ic_face_positive
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_contact
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_contact_email
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_data_source
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_data_source_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_feedback_write_a_review
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_leave_feedback
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_leave_feedback_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_report_issue
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_report_issue_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_source_code
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_source_code_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_store_review_message
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * External link displayed on the about screen
 *
 * @param url Address opened on click.
 * @param iconRes Leading icon of the link.
 * @param titleRes Title of the link.
 * @param messageRes Short description of the link.
 */
enum class AboutLink(
    val url: String,
    val iconRes: DrawableResource,
    val titleRes: StringResource,
    val messageRes: StringResource,
) {
    STORE_REVIEW(
        url = getStoreAppUrl(),
        iconRes = Res.drawable.ic_face_positive,
        titleRes = Res.string.lbl_feedback_write_a_review,
        messageRes = Res.string.lbl_store_review_message,
    ),
    REPORT_ISSUE(
        url = "https://github.com/SemenciucCosmin/Orar-UBB-FMI/issues/new",
        iconRes = Res.drawable.ic_face_negative,
        titleRes = Res.string.lbl_report_issue,
        messageRes = Res.string.lbl_report_issue_message,
    ),
    LEAVE_FEEDBACK(
        url = "https://forms.gle/T1ZK6aB3NCVphqfZ8",
        iconRes = Res.drawable.ic_face_positive,
        titleRes = Res.string.lbl_leave_feedback,
        messageRes = Res.string.lbl_leave_feedback_message,
    ),
    CONTACT(
        url = "mailto:cosminsemenciuc@gmail.com",
        iconRes = Res.drawable.ic_custom,
        titleRes = Res.string.lbl_contact,
        messageRes = Res.string.lbl_contact_email,
    ),
    REPOSITORY(
        url = "https://github.com/SemenciucCosmin/Orar-UBB-FMI",
        iconRes = Res.drawable.ic_explore,
        titleRes = Res.string.lbl_source_code,
        messageRes = Res.string.lbl_source_code_message,
    ),
    DATA_SOURCE(
        url = "https://www.cs.ubbcluj.ro",
        iconRes = Res.drawable.ic_building,
        titleRes = Res.string.lbl_data_source,
        messageRes = Res.string.lbl_data_source_message,
    ),
}
