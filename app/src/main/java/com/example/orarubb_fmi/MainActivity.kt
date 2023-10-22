package com.example.orarubb_fmi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.orarubb_fmi.ui.theme.OrarUBBFMITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrarUBBFMITheme {
                OrarUbbFmiApp()
            }
        }
    }
}
