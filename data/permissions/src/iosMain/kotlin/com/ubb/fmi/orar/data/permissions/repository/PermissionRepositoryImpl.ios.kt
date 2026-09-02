package com.ubb.fmi.orar.data.permissions.repository

import com.ubb.fmi.orar.data.permissions.model.Permission
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.coroutines.resume

class PermissionRepositoryImpl : PermissionRepository {

    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()

    override suspend fun isGranted(permission: Permission): Boolean = when (permission) {
        Permission.NOTIFICATIONS -> suspendCancellableCoroutine { cont ->
            notificationCenter.getNotificationSettingsWithCompletionHandler { settings ->
                cont.resume(settings?.authorizationStatus == UNAuthorizationStatusAuthorized)
            }
        }
    }

    override suspend fun request(permission: Permission): Boolean = when (permission) {
        Permission.NOTIFICATIONS -> suspendCancellableCoroutine { cont ->
            notificationCenter.requestAuthorizationWithOptions(
                options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge,
            ) { granted, _ -> cont.resume(granted) }
        }
    }
}
