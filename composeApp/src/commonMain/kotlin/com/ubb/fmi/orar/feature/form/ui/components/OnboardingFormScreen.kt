package com.ubb.fmi.orar.feature.form.ui.components

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
import com.ubb.fmi.orar.feature.form.ui.model.Semester
import com.ubb.fmi.orar.ui.catalog.model.UserType
import com.ubb.fmi.orar.ui.catalog.components.FormInputItem
import com.ubb.fmi.orar.ui.catalog.components.FormInputListItem
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OnboardingFormScreen(
    studyLevels: List<Int>,
    selectedStudyLevel: Int?,
    selectedSemesterId: String?,
    selectedUserTypeId: String?,
    isNextEnabled: Boolean,
    onStudyLevelClick: (Int) -> Unit,
    onSemesterClick: (String) -> Unit,
    onUserTypeClick: (String) -> Unit,
    onNextClick: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(
                text = "Bun venit!",
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
                    color = MaterialTheme.colorScheme.onSurface
                )

                FormInputListItem(
                    title = "Anul",
                    selectedItemId = selectedStudyLevel,
                    onItemClick = onStudyLevelClick,
                    items = studyLevels.map { FormInputItem(it, "$it-${it.inc()}") }
                )

                HorizontalDivider()

                FormInputListItem(
                    title = "Semestrul",
                    selectedItemId = selectedSemesterId,
                    onItemClick = onSemesterClick,
                    items = Semester.entries.map { FormInputItem(it.id, it.label) }
                )

                HorizontalDivider()

                FormInputListItem(
                    title = "Utilizator",
                    selectedItemId = selectedUserTypeId,
                    onItemClick = onUserTypeClick,
                    items = UserType.entries.map { FormInputItem(it.id, it.label) }
                )
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
            studyLevels = listOf(2024, 2025),
            selectedStudyLevel = 2025,
            selectedSemesterId = Semester.FIRST.id,
            selectedUserTypeId = UserType.TEACHER.id,
            isNextEnabled = true,
            onStudyLevelClick = {},
            onSemesterClick = {},
            onUserTypeClick = {},
            onNextClick = {}
        )
    }
}
