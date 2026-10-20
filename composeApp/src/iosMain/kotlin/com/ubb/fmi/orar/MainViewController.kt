package com.ubb.fmi.orar

import Logger
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.ubb.fmi.orar.app.AppGraph
import com.ubb.fmi.orar.app.AppInitializer
import com.ubb.fmi.orar.di.KoinInitializer
import com.ubb.fmi.orar.domain.colorpack.model.ColorPack
import com.ubb.fmi.orar.domain.colorpack.usecase.GetColorPackUseCase
import com.ubb.fmi.orar.domain.theme.model.ThemeOption
import com.ubb.fmi.orar.domain.theme.usecase.GetThemeOptionUseCase
import com.ubb.fmi.orar.feature.dialogs.ui.route.DialogsRoute
import com.ubb.fmi.orar.ui.colorpack.ColorPackProvider
import com.ubb.fmi.orar.ui.navigation.destination.MainNavDestination
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import org.koin.compose.koinInject
import platform.UIKit.UIApplication
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene

/**
 * Main view controller for the Orar UBB FMI application on iOS.
 * This function initializes Koin and sets up the main content view using Compose.
 */
@Suppress("FunctionNaming")
fun MainViewController() = ComposeUIViewController(
    configure = {
        KoinInitializer.initKoin()
        AppInitializer().initApp()
    }
) {
    val navController = rememberNavController()
    val getThemeOptionUseCase: GetThemeOptionUseCase = koinInject()
    val logger: Logger = koinInject()
    val themeOption by getThemeOptionUseCase().collectAsStateWithLifecycle(
        initialValue = ThemeOption.SYSTEM
    )
    val getColorPackUseCase: GetColorPackUseCase = koinInject()
    val colorPackFlow = remember(getColorPackUseCase) { getColorPackUseCase() }
    val colorPack by colorPackFlow.collectAsStateWithLifecycle(
        initialValue = ColorPack.Default
    )

    LaunchedEffect(themeOption) {
        applyUserInterfaceStyle(themeOption)
    }

    val deepLinkUrl = DeepLinkHandler.deepLinkUrl
    LaunchedEffect(deepLinkUrl) {
        if (deepLinkUrl == null) return@LaunchedEffect
        val eventId = extractEventIdFromDeepLink(deepLinkUrl)
        logger.d(DEEP_LINK_TAG, "Received deep link $deepLinkUrl, eventId: $eventId")

        eventId?.let {
            navController.navigate(MainNavDestination.UserMain(eventId)) {
                popUpTo(MainNavDestination.UserMain()) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    OrarUbbFmiTheme(themeOption) {
        ColorPackProvider(colorPack) {
            AppGraph(navController)
            DialogsRoute(navController)
        }
    }
}

private const val DEEP_LINK_TAG = "DeepLinkHandler"

/**
 * Overrides the interface style of the app windows so system UI, like the status bar,
 * follows the in-app [themeOption] instead of the system setting.
 */
private fun applyUserInterfaceStyle(themeOption: ThemeOption) {
    val style = when (themeOption) {
        ThemeOption.DARK -> UIUserInterfaceStyle.UIUserInterfaceStyleDark
        ThemeOption.LIGHT -> UIUserInterfaceStyle.UIUserInterfaceStyleLight
        ThemeOption.SYSTEM -> UIUserInterfaceStyle.UIUserInterfaceStyleUnspecified
    }

    UIApplication.sharedApplication.connectedScenes
        .filterIsInstance<UIWindowScene>()
        .flatMap { scene -> scene.windows.filterIsInstance<UIWindow>() }
        .forEach { window -> window.overrideUserInterfaceStyle = style }
}

private fun extractEventIdFromDeepLink(url: String): String? {
    val query = url.substringAfter("?", "")
    if (query.isBlank()) return null
    return query.split("&")
        .asSequence()
        .mapNotNull { queryPart ->
            val separatorIndex = queryPart.indexOf("=")
            if (separatorIndex <= 0) return@mapNotNull null
            queryPart.substring(0, separatorIndex) to queryPart.substring(separatorIndex + 1)
        }
        .firstOrNull { (key, _) -> key == "eventId" }
        ?.second
        ?.takeIf { it.isNotBlank() }
}
