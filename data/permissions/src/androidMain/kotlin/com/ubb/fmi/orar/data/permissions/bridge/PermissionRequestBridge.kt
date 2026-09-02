package com.ubb.fmi.orar.data.permissions.bridge

import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Bridges runtime permission requests between the data layer (which has no Activity
 * reference) and the hosting Activity (which must launch the system permission dialog
 * and owns the [ActivityResultLauncher]).
 *
 * The hosting Activity must call [register] in `onCreate` with a launcher created via
 * `registerForActivityResult(ActivityResultContracts.RequestPermission())`, forward the
 * callback result to [onResult], and call [unregister] in `onDestroy`.
 */
object PermissionRequestBridge {

    private var launcher: ActivityResultLauncher<String>? = null
    private var continuation: CancellableContinuation<Boolean>? = null

    fun register(launcher: ActivityResultLauncher<String>) {
        this.launcher = launcher
    }

    fun unregister() {
        launcher = null
        continuation?.cancel()
        continuation = null
    }

    suspend fun request(androidPermission: String): Boolean {
        val currentLauncher = launcher ?: return false

        return suspendCancellableCoroutine { cont ->
            continuation = cont
            cont.invokeOnCancellation { continuation = null }
            currentLauncher.launch(androidPermission)
        }
    }

    fun onResult(granted: Boolean) {
        continuation?.resume(granted) { }
        continuation = null
    }
}
