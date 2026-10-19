package com.ubb.fmi.orar.feature.settings.viewmodel.model

import com.ubb.fmi.orar.feature.settings.ui.model.AboutLink
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Ui state of the about screen.
 *
 * @param supportLinks Links for reviewing the app, reporting issues, giving feedback and
 * contacting the developer.
 * @param projectLinks Links about the project and its data.
 */
data class AboutUiState(
    val supportLinks: ImmutableList<AboutLink> = persistentListOf(
        AboutLink.STORE_REVIEW,
        AboutLink.REPORT_ISSUE,
        AboutLink.LEAVE_FEEDBACK,
        AboutLink.CONTACT,
    ),
    val projectLinks: ImmutableList<AboutLink> = persistentListOf(
        AboutLink.REPOSITORY,
        AboutLink.DATA_SOURCE,
    ),
)
