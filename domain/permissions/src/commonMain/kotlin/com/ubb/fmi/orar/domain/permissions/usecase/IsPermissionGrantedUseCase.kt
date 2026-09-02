package com.ubb.fmi.orar.domain.permissions.usecase

import com.ubb.fmi.orar.data.permissions.model.Permission
import com.ubb.fmi.orar.data.permissions.repository.PermissionRepository

/**
 * Checks whether [permission] is currently granted, without prompting the user.
 */
class IsPermissionGrantedUseCase(
    private val permissionRepository: PermissionRepository,
) {
    suspend operator fun invoke(permission: Permission): Boolean {
        return permissionRepository.isGranted(permission)
    }
}
