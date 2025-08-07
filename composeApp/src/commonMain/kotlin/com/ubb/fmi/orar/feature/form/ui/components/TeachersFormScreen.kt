package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.feature.form.viewmodel.model.TeachersFormUiState
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay

@Composable
fun TeachersFormScreen(
    uiState: TeachersFormUiState,
    onTeacherClick: (String) -> Unit,
    onNextClick: () -> Unit,
    onRetryClick: () -> Unit,
) {
    Scaffold { paddingValues ->
        when {
            uiState.isLoading -> {
                ProgressOverlay(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                )
            }

            uiState.isError -> {
                FailureState(
                    onRetry = onRetryClick,
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                )
            }

            else -> {
                Column(modifier = Modifier.padding(paddingValues)) {
                    TeachersFormList(
                        teachers = uiState.teachers,
                        selectedTeacherId = uiState.selectedTeacherId,
                        onTeacherClick = onTeacherClick,
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.weight(1f)
                    )


                    Button(
                        onClick = onNextClick,
                        enabled = uiState.selectedTeacherId != null,
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
    }
}
