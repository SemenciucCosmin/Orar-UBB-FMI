package com.ubb.fmi.orar.data.network.model

import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException

const val HTTP_UNPROCESSABLE_ENTITY = 422
const val HTTP_UNAUTHORIZED = 401
const val HTTP_FORBIDDEN = 403
const val HTTP_INTERNAL_ERROR = 500
const val HTTP_GONE = 410
const val HTTP_NOT_FOUND = 410
val HTTP_OK = 200..299

suspend inline fun <reified T> processApiResource(
    operation: suspend () -> HttpResponse
): Resource<T> {
    return try {
        val response = operation.invoke()
        when (response.status.value) {
            in HTTP_OK -> {
                try {
                    Resource(response.body<T>(), Status.Success)
                } catch (_: NoTransformationFoundException) {
                    Resource(null, Status.UnprocessableEntity)
                }
            }

            in listOf(HTTP_UNAUTHORIZED, HTTP_FORBIDDEN) -> {
                Resource(null, Status.AuthorizationError)
            }

            HTTP_UNPROCESSABLE_ENTITY -> {
                Resource(null, Status.UnprocessableEntity)
            }

            HTTP_INTERNAL_ERROR -> {
                Resource(null, Status.ServerError)
            }

            HTTP_GONE -> {
                Resource(null, Status.GoneError)
            }

            HTTP_NOT_FOUND -> {
                Resource(null, Status.NotFoundError)
            }

            else -> {
                Resource(null, Status.Error)
            }
        }
    } catch (exception: CancellationException) {
        // We want only to log this exception event.
        // Throw it back in order not to intervene with the native coroutine flow or work.
        throw exception
    } catch (_: IOException) {
        // Error occurred while communicating to the server.
        Resource(null, Status.NetworkError)
    } catch (_: Throwable) {
        // An internal error occurred while attempting to execute a request.
        Resource(null, Status.Error)
    }
}
