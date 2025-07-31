package com.ubb.fmi.orar.network.model

sealed class Status {

    data object Loading : Status()

    data object Success : Status()

    data object Empty : Status()

    data object NotFoundError : Status()

    data object Error : Status()

    data object NetworkError : Status()

    data object AuthorizationError : Status()

    data object ServerError : Status()

    data object GoneError : Status()

    data object UnprocessableEntity : Status()

    data object ResourceCancellation : Status()
}

fun Status.isSuccess() = this is Status.Success

fun Status.isEmpty() = this is Status.Empty

fun Status.isLoading() = this is Status.Loading

fun Status.isError(): Boolean = this !in listOf(Status.Success, Status.Loading, Status.Empty)