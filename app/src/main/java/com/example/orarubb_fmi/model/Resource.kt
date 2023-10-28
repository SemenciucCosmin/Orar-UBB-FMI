package com.example.orarubb_fmi.model

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()

    data class Error<T>(val type: ErrorType) : Resource<T>()

    sealed interface ErrorType {
        data object NotFound : ErrorType
        data object Network : ErrorType
    }
}
