package com.ubb.fmi.orar.data.permissions.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.ubb.fmi.orar.data.permissions.bridge.PermissionRequestBridge
import com.ubb.fmi.orar.data.permissions.model.Permission

class PermissionRepositoryImpl(
    private val context: Context,
) : PermissionRepository {

    override suspend fun isGranted(permission: Permission): Boolean {
        val androidPermission = permission.toAndroidPermissionOrNull() ?: return true
        return ContextCompat.checkSelfPermission(
            context,
            androidPermission,
        ) == PackageManager.PERMISSION_GRANTED
    }

    override suspend fun request(permission: Permission): Boolean {
        if (isGranted(permission)) return true
        val androidPermission = permission.toAndroidPermissionOrNull() ?: return true
        return PermissionRequestBridge.request(androidPermission)
    }

    /**
     * Maps a common [Permission] to its Android manifest permission string.
     * Returns null when the permission isn't required on the current API level
     * (e.g. POST_NOTIFICATIONS is only enforced starting Android 13/API 33).
     */
    private fun Permission.toAndroidPermissionOrNull(): String? = when (this) {
        Permission.NOTIFICATIONS -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.POST_NOTIFICATIONS
            } else {
                null
            }
        }
    }
}
