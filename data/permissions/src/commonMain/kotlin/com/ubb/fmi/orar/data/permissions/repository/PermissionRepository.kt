package com.ubb.fmi.orar.data.permissions.repository

import com.ubb.fmi.orar.data.permissions.model.Permission

/**
 * Exposes a single, platform-agnostic API for checking and requesting runtime permissions.
 * Android and iOS implementations hide all platform-specific plumbing behind [isGranted]
 * and [request].
 */
interface PermissionRepository {

    /**
     * Returns whether [permission] is currently granted, without prompting the user.
     */
    suspend fun isGranted(permission: Permission): Boolean

    /**
     * Requests [permission] from the user if it isn't already granted.
     * Returns whether the permission ends up granted.
     */
    suspend fun request(permission: Permission): Boolean
}
