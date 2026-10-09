package com.ubb.fmi.orar.data.permissions.repository

import Logger
import com.ubb.fmi.orar.data.permissions.model.Permission
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.coroutines.resume

/**
 * iOS [PermissionRepository] implementation backed by `UNUserNotificationCenter`'s
 * authorization APIs.
 */
class PermissionRepositoryImpl(
    private val logger: Logger,
) : PermissionRepository {

    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()

    override suspend fun isGranted(permission: Permission): Boolean = when (permission) {
        Permission.NOTIFICATIONS -> suspendCancellableCoroutine { cont ->
            notificationCenter.getNotificationSettingsWithCompletionHandler { settings ->
                val status = settings?.authorizationStatus
                logger.d(
                    TAG,
                    "Notification authorization status: $status (authorized = $UNAuthorizationStatusAuthorized)"
                )
                cont.resume(status == UNAuthorizationStatusAuthorized)
            }
        }
    }

    override suspend fun request(permission: Permission): Boolean = when (permission) {
        Permission.NOTIFICATIONS -> suspendCancellableCoroutine { cont ->
            notificationCenter.requestAuthorizationWithOptions(
                options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge,
            ) { granted, error ->
                when (error) {
                    null -> logger.d(TAG, "Notification authorization request result, granted: $granted")
                    else -> logger.e(TAG, "Notification authorization request failed: ${error.localizedDescription}")
                }
                cont.resume(granted)
            }
        }
    }

    companion object {
        private const val TAG = "PermissionRepository"
    }
}
