package com.ubb.fmi.orar.feature.timetable.ui.route

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.ubb.fmi.orar.ui.navigation.components.TimetableBottomBar

@Composable
fun HomeRoute(navController: NavController) {
    Scaffold(
        bottomBar = { TimetableBottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Main timetable")
        }
    }
}
