package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.data.timetable.model.Semester
import com.ubb.fmi.orar.data.timetable.model.UserType
import com.ubb.fmi.orar.ui.catalog.components.PrimaryButton
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.list.ChipSelectionRow
import com.ubb.fmi.orar.ui.catalog.extensions.labelRes
import com.ubb.fmi.orar.ui.catalog.model.Chip
import com.ubb.fmi.orar.ui.catalog.model.ConfigurationFormType
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_next
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_onboarding_semester_details
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_onboarding_user_type
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_onboarding_user_type_details
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_onboarding_year_details
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_semester
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_timetable_configuration_title
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_year
import org.jetbrains.compose.resources.stringResource

/**
 * Composable screen with multiple available choices for timetable configuration.
 * @param configurationFormType: type of the form, either STARTUP or SETTINGS
 * @param studyYears: list of available study years
 * @param selectedStudyYear: currently selected study year
 * @param selectedSemesterId: currently selected semester id
 * @param selectedUserTypeId: currently selected user type id
 * @param isNextEnabled: flag to enable or disable the next button
 * @param onStudyYearClick: lambda for study year selection
 * @param onSemesterClick: lambda for semester selection
 * @param onUserTypeClick: lambda for user type selection
 * @param onNextClick: lambda for next button action
 * @param onBack: lambda for back button action
 */
@Suppress("LongParameterList")
@Composable
fun OnboardingFormScreen(
    configurationFormType: ConfigurationFormType,
    studyYears: List<Int>,
    selectedStudyYear: Int?,
    selectedSemesterId: String?,
    selectedUserTypeId: String?,
    isNextEnabled: Boolean,
    onStudyYearClick: (Int) -> Unit,
    onSemesterClick: (String) -> Unit,
    onUserTypeClick: (String) -> Unit,
    onNextClick: () -> Unit,
    onBack: () -> Unit,
) {
    val chipsStartPadding = Pds.icon.Medium + Pds.spacing.SMedium

    Scaffold(
        topBar = {
            if (configurationFormType == ConfigurationFormType.SETTINGS) {
                TopBar(
                    title = stringResource(Res.string.lbl_timetable_configuration_title),
                    onBack = onBack
                )
            }
        },
        bottomBar = {
            PrimaryButton(
                onClick = onNextClick,
                enabled = isNextEnabled,
                text = stringResource(Res.string.lbl_next),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(Pds.spacing.Medium)
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.Large),
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(Pds.spacing.Medium)
        ) {
            if (configurationFormType == ConfigurationFormType.STARTUP) {
                OnboardingHeader()
            }

            OnboardingStep(
                number = 1,
                title = stringResource(Res.string.lbl_onboarding_user_type),
                description = stringResource(Res.string.lbl_onboarding_user_type_details),
                isCompleted = selectedUserTypeId != null,
            ) {
                ChipSelectionRow(
                    selectedChipId = selectedUserTypeId,
                    onClick = onUserTypeClick,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.padding(start = chipsStartPadding),
                    chips = UserType.entries.map {
                        Chip(it.id, stringResource(it.labelRes))
                    },
                )
            }

            OnboardingStep(
                number = 2,
                title = stringResource(Res.string.lbl_year),
                description = stringResource(Res.string.lbl_onboarding_year_details),
                isCompleted = selectedStudyYear != null,
            ) {
                ChipSelectionRow(
                    selectedChipId = selectedStudyYear,
                    onClick = onStudyYearClick,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.padding(start = chipsStartPadding),
                    chips = studyYears.map { year -> Chip(year, "$year-${year.inc()}") },
                )
            }

            OnboardingStep(
                number = 3,
                title = stringResource(Res.string.lbl_semester),
                description = stringResource(Res.string.lbl_onboarding_semester_details),
                isCompleted = selectedSemesterId != null,
            ) {
                ChipSelectionRow(
                    selectedChipId = selectedSemesterId,
                    onClick = onSemesterClick,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.padding(start = chipsStartPadding),
                    chips = Semester.entries.map {
                        Chip(it.id, stringResource(it.labelRes))
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewOnboardingFormScreen() {
    OrarUbbFmiTheme {
        OnboardingFormScreen(
            configurationFormType = ConfigurationFormType.STARTUP,
            studyYears = listOf(2024, 2025),
            selectedStudyYear = 2025,
            selectedSemesterId = Semester.FIRST.id,
            selectedUserTypeId = UserType.TEACHER.id,
            isNextEnabled = true,
            onStudyYearClick = {},
            onSemesterClick = {},
            onUserTypeClick = {},
            onNextClick = {},
            onBack = {}
        )
    }
}
