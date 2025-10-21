package com.ubb.fmi.orar.data.network.service

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.processApiResource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/**
 * Api service for fetching news posts
 */
class NewsApi(private val httpClient: HttpClient) {

    /**
     * Fetch news posts for students
     */
    suspend fun getStudentNewsHtml(): Resource<String> {
        return processApiResource {
            httpClient.get("$BASE_URL/anunturi-studenti/")
        }
    }

    /**
     * Fetch news posts for teachers
     */
    suspend fun getTeacherNewsHtml(): Resource<String> {
        return processApiResource {
            httpClient.get("$BASE_URL/anunturi-cadre-didactice/")
        }
    }

    companion object {
        private const val BASE_URL = "https://www.cs.ubbcluj.ro/anunturi"
    }
}