package com.ubb.fmi.orar.app

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.ubb.fmi.orar.data.permissions.bridge.PermissionRequestBridge
import com.ubb.fmi.orar.domain.colorpack.model.ColorPack
import com.ubb.fmi.orar.domain.colorpack.usecase.GetColorPackUseCase
import com.ubb.fmi.orar.domain.theme.model.ThemeOption
import com.ubb.fmi.orar.domain.theme.usecase.GetThemeOptionUseCase
import com.ubb.fmi.orar.feature.dialogs.ui.route.DialogsRoute
import com.ubb.fmi.orar.ui.colorpack.ColorPackProvider
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Main activity for the Orar UBB FMI application.
 * This activity sets up the main content view and applies the app theme.
 */
class MainActivity : ComponentActivity(), KoinComponent {

    private val getThemeOptionUseCase: GetThemeOptionUseCase by inject()
    private val getColorPackUseCase: GetColorPackUseCase by inject()

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
            val colorPackFlow = remember { getColorPackUseCase() }
            val colorPack by colorPackFlow.collectAsStateWithLifecycle(
                initialValue = ColorPack.Default
            )

            val nightMode = LocalConfiguration.current.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val isDarkTheme = when (themeOption) {
                ThemeOption.DARK -> true
                ThemeOption.LIGHT -> false
                ThemeOption.SYSTEM -> nightMode == Configuration.UI_MODE_NIGHT_YES
            }

            LaunchedEffect(isDarkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        lightScrim = Color.TRANSPARENT,
                        darkScrim = Color.TRANSPARENT,
                        detectDarkMode = { isDarkTheme },
                    ),
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim = LIGHT_NAVIGATION_BAR_SCRIM,
                        darkScrim = DARK_NAVIGATION_BAR_SCRIM,
                        detectDarkMode = { isDarkTheme },
                    ),
                )
            }

            OrarUbbFmiTheme(themeOption) {
                ColorPackProvider(colorPack) {
                    AppGraph(navController)
                    DialogsRoute(navController)
                }
            }
        }
    }

    override fun onDestroy() {
        PermissionRequestBridge.unregister()
        super.onDestroy()
    }

    companion object {
        // Same scrims used by the default enableEdgeToEdge() for 3-button navigation
        private val LIGHT_NAVIGATION_BAR_SCRIM = Color.argb(0xE6, 0xFF, 0xFF, 0xFF)
        private val DARK_NAVIGATION_BAR_SCRIM = Color.argb(0x80, 0x1B, 0x1B, 0x1B)
    }
}
