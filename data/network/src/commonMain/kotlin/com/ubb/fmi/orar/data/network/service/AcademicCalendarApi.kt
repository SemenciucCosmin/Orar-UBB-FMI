package com.ubb.fmi.orar.data.network.service

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.processApiResource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/**
 * Api service for fetching academic calendar
 */
class AcademicCalendarApi(private val httpClient: HttpClient) {

    /**
     * Fetch academic calendar for current year
     */
    suspend fun getCalendar(): Resource<String> {
        return processApiResource {
            httpClient.get("$BASE_URL/invatamant/structura-anului-universitar/")
        }
    }

    companion object {
        private const val BASE_URL = "https://www.cs.ubbcluj.ro"
    }
}