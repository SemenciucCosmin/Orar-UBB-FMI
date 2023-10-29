package com.example.orarubb_fmi.data.network

sealed class CallResponse<out T : Any> {
    data class Success<out T : Any>(val data: T) : CallResponse<T>()
    data class Error(val exception: Exception) : CallResponse<Nothing>()
}
