package com.ubb.fmi.orar.data.network.model

/**
 * Wrapper for transporting information along with a status at data level
 * @param [payload] actual data with generic type
 * @param [status] status of the process that fetched the data
 */
data class Resource<T>(val payload: T?, val status: Status)
