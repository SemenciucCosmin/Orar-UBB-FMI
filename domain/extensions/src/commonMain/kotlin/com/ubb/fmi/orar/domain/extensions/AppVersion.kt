package com.ubb.fmi.orar.domain.extensions

/**
 * Returns the current app version using [platformContext]
 */
expect fun getAppVersion(platformContext: Any?): String?