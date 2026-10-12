package com.ubb.fmi.orar.domain.permissions.usecase

import com.ubb.fmi.orar.data.permissions.model.Permission
import com.ubb.fmi.orar.data.permissions.repository.PermissionRepository

/**
 * Opens the system settings screen where the user can grant [Permission] manually.
 */
class OpenPermissionSettingsUseCase(
    private val permissionRepository: PermissionRepository,
) {
    operator fun invoke(permission: Permission) {
        permissionRepository.openSettings(permission)
    }
}
