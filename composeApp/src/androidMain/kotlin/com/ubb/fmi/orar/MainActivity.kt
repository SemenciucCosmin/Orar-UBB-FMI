package com.ubb.fmi.orar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ubb.fmi.orar.app.AppGraph
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme

/**
 * Main activity for the Orar UBB FMI application.
 * This activity sets up the main content view and applies the app theme.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            OrarUbbFmiTheme {
                AppGraph()
            }
        }
    }
}
