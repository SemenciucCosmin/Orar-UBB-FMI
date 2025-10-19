package com.ubb.fmi.orar.ui.catalog.extensions

import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus

/**
 * Converts [Status] to ui [ErrorStatus]
 */
fun Status.toErrorStatus(): ErrorStatus? {
    return when (this) {
        Status.Loading, Status.Success, Status.Empty -> null
        Status.NetworkError -> ErrorStatus.NETWORK
        Status.NotFoundError -> ErrorStatus.NOT_FOUND
        else -> ErrorStatus.GENERIC
    }
}
