package com.ubb.fmi.orar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.ubb.fmi.orar.app.AppGraph
import com.ubb.fmi.orar.domain.theme.model.ThemeOption
import com.ubb.fmi.orar.domain.theme.usecase.GetThemeOptionUseCase
import com.ubb.fmi.orar.feature.dialogs.ui.route.DialogsRoute
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Main activity for the Orar UBB FMI application.
 * This activity sets up the main content view and applies the app theme.
 */
class MainActivity : ComponentActivity(), KoinComponent {

    private val getThemeOptionUseCase: GetThemeOptionUseCase by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            val navController = rememberNavController()
            val themeOption by getThemeOptionUseCase().collectAsStateWithLifecycle(
                initialValue = ThemeOption.SYSTEM
            )

            OrarUbbFmiTheme(themeOption) {
                AppGraph(navController)
                DialogsRoute(navController)
            }
        }
    }
}
