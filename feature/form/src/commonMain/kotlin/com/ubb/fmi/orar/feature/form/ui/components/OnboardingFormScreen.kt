@file:Suppress("MagicNumber")

package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.ui.catalog.model.ConfigurationFormType
import com.ubb.fmi.orar.ui.catalog.model.Semester
import com.ubb.fmi.orar.ui.catalog.components.PrimaryButton
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.list.FormSelectionRow
import com.ubb.fmi.orar.ui.catalog.components.state.StateScaffold
import com.ubb.fmi.orar.ui.catalog.model.FormSelectionItem
import com.ubb.fmi.orar.ui.catalog.model.UserType
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.feature.form.generated.resources.Res
import orar_ubb_fmi.feature.form.generated.resources.lbl_next
import orar_ubb_fmi.feature.form.generated.resources.lbl_semester
import orar_ubb_fmi.feature.form.generated.resources.lbl_timetable_configuration_message
import orar_ubb_fmi.feature.form.generated.resources.lbl_timetable_configuration_title
import orar_ubb_fmi.feature.form.generated.resources.lbl_user
import orar_ubb_fmi.feature.form.generated.resources.lbl_welcome
import orar_ubb_fmi.feature.form.generated.resources.lbl_year
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingFormScreen(
    configurationFormType: ConfigurationFormType,
    studyLevels: List<Int>,
    selectedStudyLevel: Int?,
    selectedSemesterId: String?,
    selectedUserTypeId: String?,
    isNextEnabled: Boolean,
    onStudyLevelClick: (Int) -> Unit,
    onSemesterClick: (String) -> Unit,
    onUserTypeClick: (String) -> Unit,
    onNextClick: () -> Unit,
    onBack: () -> Unit,
) {
    StateScaffold(
        topBar = {
            if (configurationFormType == ConfigurationFormType.SETTINGS) {
                TopBar(
                    title = stringResource(Res.string.lbl_timetable_configuration_title),
                    onBack = onBack
                )
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (configurationFormType == ConfigurationFormType.STARTUP) {
                Text(
                    text = stringResource(Res.string.lbl_welcome),
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(Pds.spacing.Medium)
                        .fillMaxWidth()
                        .weight(0.2f)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
                modifier = Modifier
                    .weight(0.8f)
                    .padding(horizontal = Pds.spacing.Medium),
            ) {
                Text(
                    text = stringResource(Res.string.lbl_timetable_configuration_message),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                FormSelectionRow(
                    headline = stringResource(Res.string.lbl_year),
                    selectedItemId = selectedStudyLevel,
                    onClick = onStudyLevelClick,
                    items = studyLevels.map { level ->
                        FormSelectionItem(
                            id = level,
                            label = "$level-${level.inc()}"
                        )
                    }
                )

                HorizontalDivider()

                FormSelectionRow(
                    headline = stringResource(Res.string.lbl_semester),
                    selectedItemId = selectedSemesterId,
                    onClick = onSemesterClick,
                    items = Semester.entries.map {
                        FormSelectionItem(
                            id = it.id,
                            label = stringResource(it.labelRes)
                        )
                    }
                )

                HorizontalDivider()

                FormSelectionRow(
                    headline = stringResource(Res.string.lbl_user),
                    selectedItemId = selectedUserTypeId,
                    onClick = onUserTypeClick,
                    items = UserType.entries.map {
                        FormSelectionItem(
                            id = it.id,
                            label = stringResource(it.labelRes)
                        )
                    }
                )
            }

            PrimaryButton(
                onClick = onNextClick,
                enabled = isNextEnabled,
                text = stringResource(Res.string.lbl_next),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Pds.spacing.Medium)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewOnboardingFormScreen() {
    OrarUbbFmiTheme {
        OnboardingFormScreen(
            configurationFormType = ConfigurationFormType.STARTUP,
            studyLevels = listOf(2024, 2025),
            selectedStudyLevel = 2025,
            selectedSemesterId = Semester.FIRST.id,
            selectedUserTypeId = UserType.TEACHER.id,
            isNextEnabled = true,
            onStudyLevelClick = {},
            onSemesterClick = {},
            onUserTypeClick = {},
            onNextClick = {},
            onBack = {}
        )
    }
}
