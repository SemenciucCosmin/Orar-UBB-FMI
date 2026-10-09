package com.ubb.fmi.orar.app.notifications

import Logger
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ubb.fmi.orar.domain.notifications.usecase.RescheduleNotificationsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Restores scheduled event notifications after a device reboot or app update.
 *
 * Android clears every [android.app.AlarmManager] alarm when the device restarts, and since
 * notifications are only rescheduled when the app process actually runs, a user who doesn't
 * reopen the app before their next class would otherwise never see it. This receiver reruns
 * scheduling straight from the cached "notifications on" events, without needing the app UI
 * or a network call.
 */
class BootReceiver : BroadcastReceiver(), KoinComponent {

    private val rescheduleNotificationsUseCase: RescheduleNotificationsUseCase by inject()
    private val logger: Logger by inject()

    @Suppress("TooGenericExceptionCaught")
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED &&
            intent.action != Intent.ACTION_MY_PACKAGE_REPLACED
        ) {
            return
        }

        logger.d(TAG, "Received ${intent.action}, restoring cached notifications")
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.Default).launch {
            try {
                rescheduleNotificationsUseCase()
                logger.d(TAG, "Restored cached notifications after ${intent.action}")
            } catch (exception: Exception) {
                logger.e(TAG, "Failed to restore notifications after ${intent.action}: ${exception.stackTraceToString()}")
            } finally {
                pendingResult.finish()
            }
        }
    }

    companion object {
        private const val TAG = "BootReceiver"
    }
}
