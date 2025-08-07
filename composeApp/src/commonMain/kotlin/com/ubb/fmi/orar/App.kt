package com.ubb.fmi.orar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
//        Column (
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Button(onClick = viewModel::test) {
//                Text(text = "Test")
//            }
//        }
        ConfigurationGraph(
            navController = navController,
            onboardingFormTitle = "Bun venit!"
        )
    }
}