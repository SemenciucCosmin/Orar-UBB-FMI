package com.ubb.fmi.orar.feature.feedback.ui.model

import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_face_negative
import orar_ubb_fmi.ui.catalog.generated.resources.ic_face_neutral
import orar_ubb_fmi.ui.catalog.generated.resources.ic_face_positive
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_feedback_choice_great
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_feedback_choice_negative_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_feedback_choice_negative_title
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_feedback_choice_poor
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_feedback_choice_positive_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_feedback_choice_positive_title
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_feedback_complete_formular
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_feedback_dont_ask_again
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_feedback_no_thanks
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_feedback_write_a_review
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_ok
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * Enum class for all types of feedback possible responses with string and drawable resources.
 */
enum class FeedbackChoice(
    val id: String,
    val labelRes: StringResource,
    val titleRes: StringResource,
    val subtitleRes: StringResource,
    val actionLabelRes: StringResource,
    val secondaryActionLabelRes: StringResource?,
    val dismissLabelRes: StringResource,
    val iconRes: DrawableResource,
) {
    POOR(
        id = "poor",
        labelRes = Res.string.lbl_feedback_choice_poor,
        titleRes = Res.string.lbl_feedback_choice_negative_title,
        subtitleRes = Res.string.lbl_feedback_choice_negative_message,
        actionLabelRes = Res.string.lbl_feedback_complete_formular,
        secondaryActionLabelRes = null,
        dismissLabelRes = Res.string.lbl_feedback_no_thanks,
        iconRes = Res.drawable.ic_face_negative,
    ),
    OK(
        id = "ok",
        labelRes = Res.string.lbl_ok,
        titleRes = Res.string.lbl_feedback_choice_negative_title,
        subtitleRes = Res.string.lbl_feedback_choice_negative_message,
        actionLabelRes = Res.string.lbl_feedback_complete_formular,
        secondaryActionLabelRes = null,
        dismissLabelRes = Res.string.lbl_feedback_no_thanks,
        iconRes = Res.drawable.ic_face_neutral,
    ),
    GREAT(
        id = "great",
        labelRes = Res.string.lbl_feedback_choice_great,
        titleRes = Res.string.lbl_feedback_choice_positive_title,
        subtitleRes = Res.string.lbl_feedback_choice_positive_message,
        actionLabelRes = Res.string.lbl_feedback_write_a_review,
        secondaryActionLabelRes = Res.string.lbl_feedback_complete_formular,
        dismissLabelRes = Res.string.lbl_feedback_dont_ask_again,
        iconRes = Res.drawable.ic_face_positive,
    );

    companion object {
        fun getById(id: String): FeedbackChoice {
            return FeedbackChoice.entries.firstOrNull { it.id == id } ?: GREAT
        }
    }
}
