package com.ubb.fmi.orar.domain.permissions.usecase

import com.ubb.fmi.orar.data.permissions.model.Permission
import com.ubb.fmi.orar.data.permissions.repository.PermissionRepository

/**
 * Requests [permission] from the user, if it isn't already granted.
 * Returns whether the permission ends up granted.
 */
class RequestPermissionUseCase(
    private val permissionRepository: PermissionRepository,
) {
    suspend operator fun invoke(permission: Permission): Boolean {
        return permissionRepository.request(permission)
    }
}
