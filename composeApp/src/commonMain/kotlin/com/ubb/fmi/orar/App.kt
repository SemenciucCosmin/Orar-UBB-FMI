package com.ubb.fmi.orar

import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.ubb.fmi.orar.data.TestViewModel
import com.ubb.fmi.orar.ui.navigation.graph.ConfigurationGraph
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    val viewModel: TestViewModel = koinViewModel<TestViewModel>()
    val navController = rememberNavController()

    OrarUbbFmiTheme {
        ConfigurationGraph(
            navController = navController,
            onboardingFormTitle = "Bun venit!"
        )
    }
}