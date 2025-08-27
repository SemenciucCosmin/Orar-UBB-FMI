package com.ubb.fmi.orar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.ubb.fmi.orar.feature.app.ui.navigation.AppGraph
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            OrarUbbFmiTheme {
                AppGraph(rememberNavController())
            }
        }
    }
}
