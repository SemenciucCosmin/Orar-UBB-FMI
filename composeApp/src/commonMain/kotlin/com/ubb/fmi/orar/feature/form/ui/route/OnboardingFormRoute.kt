package com.ubb.fmi.orar.feature.form.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.form.ui.components.OnboardingFormScreen
import com.ubb.fmi.orar.feature.form.ui.viewmodel.OnboardingFormViewModel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.isNextEnabled
import com.ubb.fmi.orar.ui.catalog.model.UserType
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingFormRoute(navController: NavController) {
    val viewModel = koinViewModel<OnboardingFormViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OnboardingFormScreen(
        studyLevels = uiState.studyLevels,
        selectedStudyLevel = uiState.selectedStudyLevel,
        selectedSemesterId = uiState.selectedSemesterId,
        selectedUserTypeId = uiState.selectedUserTypeId,
        isNextEnabled = uiState.isNextEnabled,
        onStudyLevelClick = viewModel::selectStudyLevel,
        onSemesterClick = viewModel::selectSemester,
        onUserTypeClick = viewModel::selectUserType,
        onNextClick = {
            val year = uiState.selectedStudyLevel ?: return@OnboardingFormScreen
            val semesterId = uiState.selectedSemesterId ?: return@OnboardingFormScreen
            val userTypeId = uiState.selectedUserTypeId ?: return@OnboardingFormScreen
            val userType = UserType.getById(userTypeId)

            when (userType) {
                UserType.STUDENT -> {
                    navController.navigate(
                        ConfigurationFormNavDestination.StudyLinesForm(
                            year = year,
                            semesterId = semesterId
                        )
                    )
                }

                UserType.TEACHER -> {
                    navController.navigate(
                        ConfigurationFormNavDestination.TeachersForm(
                            year = year,
                            semesterId = semesterId
                        )
                    )
                }
            }
        }
    )
}
