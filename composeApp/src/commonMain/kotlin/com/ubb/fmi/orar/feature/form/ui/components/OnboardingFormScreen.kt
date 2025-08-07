package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.data.core.model.Degree
import com.ubb.fmi.orar.data.core.model.Semester
import com.ubb.fmi.orar.data.core.model.UserType
import com.ubb.fmi.orar.data.teachers.model.TeacherTitle
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OnboardingFormScreen(
    title: String,
    studyYears: List<Int>,
    selectedStudyYear: Int?,
    selectedSemesterId: String?,
    selectedUserTypeId: String?,
    selectedDegreeId: String?,
    selectedTeacherTitleId: String?,
    isNextEnabled: Boolean,
    onStudyYearClick: (Int) -> Unit,
    onSemesterClick: (String) -> Unit,
    onUserTypeClick: (String) -> Unit,
    onDegreeClick: (String) -> Unit,
    onTeacherTitleClick: (String) -> Unit,
    onNextClick: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(
                text = title,
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .weight(0.2f)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .weight(0.8f)
                    .padding(horizontal = 16.dp),
            ) {
                Text(
                    text = "Alege configuratiile pentru orarul tau",
                    style = MaterialTheme.typography.headlineMedium,
                )

                FormInput(
                    title = "Anul",
                    selectedItemId = selectedStudyYear,
                    onItemClick = onStudyYearClick,
                    items = studyYears.map { FormInputItem(it, "$it-${it.inc()}") }
                )

                HorizontalDivider()

                FormInput(
                    title = "Semestrul",
                    selectedItemId = selectedSemesterId,
                    onItemClick = onSemesterClick,
                    items = Semester.entries.map { FormInputItem(it.id, it.label) }
                )

                HorizontalDivider()

                FormInput(
                    title = "Utilizator",
                    selectedItemId = selectedUserTypeId,
                    onItemClick = onUserTypeClick,
                    items = UserType.entries.map { FormInputItem(it.id, it.label) }
                )

                AnimatedVisibility(visible = selectedUserTypeId == UserType.STUDENT.id) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        HorizontalDivider()

                        FormInput(
                            title = "Nivelul de studiu",
                            selectedItemId = selectedDegreeId,
                            onItemClick = onDegreeClick,
                            items = Degree.entries.map { FormInputItem(it.id, it.label) }
                        )
                    }
                }

                AnimatedVisibility(visible = selectedUserTypeId == UserType.TEACHER.id) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        HorizontalDivider()

                        FormInput(
                            title = "Titlul",
                            selectedItemId = selectedTeacherTitleId,
                            onItemClick = onTeacherTitleClick,
                            items = TeacherTitle.entries.map { FormInputItem(it.id, it.id) }
                        )
                    }
                }
            }

            Button(
                onClick = onNextClick,
                enabled = isNextEnabled,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "NEXT")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewOnboardingFormScreen() {
    OrarUbbFmiTheme {
        OnboardingFormScreen(
            title = "Bun venit!",
            studyYears = listOf(2024, 2025),
            selectedStudyYear = 2025,
            selectedSemesterId = Semester.FIRST.id,
            selectedUserTypeId = UserType.TEACHER.id,
            selectedDegreeId = Degree.LICENCE.id,
            selectedTeacherTitleId = TeacherTitle.PROFESSOR.id,
            isNextEnabled = true,
            onStudyYearClick = {},
            onSemesterClick = {},
            onUserTypeClick = {},
            onDegreeClick = {},
            onTeacherTitleClick = {},
            onNextClick = {}
        )
    }
}
