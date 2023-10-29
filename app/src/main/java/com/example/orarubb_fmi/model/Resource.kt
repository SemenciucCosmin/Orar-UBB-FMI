package com.example.orarubb_fmi.model

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()

    data class Error(val type: ErrorType) : Resource<Nothing>()

    sealed interface ErrorType {
        data object NotFound : ErrorType
        data object Network : ErrorType
    }

    fun getOrNull(): T? {
        return when (this) {
            is Success -> data
            is Error -> null
        }
    }
}
