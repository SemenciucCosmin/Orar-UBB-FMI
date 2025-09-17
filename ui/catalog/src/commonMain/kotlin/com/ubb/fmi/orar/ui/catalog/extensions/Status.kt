package com.ubb.fmi.orar.ui.catalog.extensions

import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus

/**
 * Converts [Status] to ui [ErrorStatus]
 */
fun Status.toErrorStatus(): ErrorStatus? {
    return when (this) {
        Status.NetworkError -> ErrorStatus.NETWORK
        Status.NotFoundError -> ErrorStatus.NOT_FOUND
        Status.Empty, Status.Loading, Status.Success -> null
        else -> ErrorStatus.GENERIC
    }
}
