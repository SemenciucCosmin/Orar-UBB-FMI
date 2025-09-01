package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.feature.form.ui.model.ConfigurationFormType
import com.ubb.fmi.orar.feature.form.ui.model.Semester
import com.ubb.fmi.orar.ui.catalog.model.UserType
import com.ubb.fmi.orar.ui.catalog.components.FormInputItem
import com.ubb.fmi.orar.ui.catalog.components.FormInputListItem
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_left_arrow
import orar_ubb_fmi.composeapp.generated.resources.lbl_timetable_configuration_title
import orar_ubb_fmi.composeapp.generated.resources.lbl_next
import orar_ubb_fmi.composeapp.generated.resources.lbl_semester
import orar_ubb_fmi.composeapp.generated.resources.lbl_timetable_configuration_message
import orar_ubb_fmi.composeapp.generated.resources.lbl_user
import orar_ubb_fmi.composeapp.generated.resources.lbl_welcome
import orar_ubb_fmi.composeapp.generated.resources.lbl_year
import org.jetbrains.compose.resources.painterResource
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
    Scaffold(
        topBar = {
            if (configurationFormType == ConfigurationFormType.SETTINGS) {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                modifier = Modifier.size(Pds.icon.Medium),
                                painter = painterResource(Res.drawable.ic_left_arrow),
                                contentDescription = null,
                            )
                        }
                    },
                    title = {
                        Text(
                            text = stringResource(Res.string.lbl_timetable_configuration_title),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
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

                FormInputListItem(
                    title = stringResource(Res.string.lbl_year),
                    selectedItemId = selectedStudyLevel,
                    onItemClick = onStudyLevelClick,
                    items = studyLevels.map { FormInputItem(it, "$it-${it.inc()}") }
                )

                HorizontalDivider()

                FormInputListItem(
                    title = stringResource(Res.string.lbl_semester),
                    selectedItemId = selectedSemesterId,
                    onItemClick = onSemesterClick,
                    items = Semester.entries.map {
                        FormInputItem(
                            id = it.id,
                            label = stringResource(it.labelRes)
                        )
                    }
                )

                HorizontalDivider()

                FormInputListItem(
                    title = stringResource(Res.string.lbl_user),
                    selectedItemId = selectedUserTypeId,
                    onItemClick = onUserTypeClick,
                    items = UserType.entries.map {
                        FormInputItem(
                            id = it.id,
                            label = stringResource(it.labelRes)
                        )
                    }
                )
            }

            Button(
                onClick = onNextClick,
                enabled = isNextEnabled,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Pds.spacing.Medium)
            ) {
                Text(text = stringResource(Res.string.lbl_next))
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
