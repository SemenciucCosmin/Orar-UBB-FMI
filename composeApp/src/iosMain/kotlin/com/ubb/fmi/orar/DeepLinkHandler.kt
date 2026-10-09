package com.ubb.fmi.orar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController

/**
 * Bridges deep link URLs coming from the iOS shell (SwiftUI's `onOpenURL`/notification taps)
 * into the already-running Compose UI, so a new deep link never requires recreating the
 * [ComposeUIViewController] itself (which would restart the whole app UI from scratch).
 */
object DeepLinkHandler {
    var deepLinkUrl: String? by mutableStateOf(null)
}