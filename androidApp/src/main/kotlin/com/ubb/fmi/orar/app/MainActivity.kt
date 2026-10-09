package com.ubb.fmi.orar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.ubb.fmi.orar.data.permissions.bridge.PermissionRequestBridge
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

    private val permissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> PermissionRequestBridge.onResult(granted) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        installSplashScreen()
        PermissionRequestBridge.register(permissionRequestLauncher)

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

    override fun onDestroy() {
        PermissionRequestBridge.unregister()
        super.onDestroy()
    }
}
