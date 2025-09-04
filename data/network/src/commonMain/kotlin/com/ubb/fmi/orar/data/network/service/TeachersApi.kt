package com.ubb.fmi.orar.data.network.service

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.processApiResource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/**
 * Api service for fetching teacher related information
 */
class TeachersApi(private val httpClient: HttpClient) {

    /**
     * Fetch teachers table in html format by [year] and [semesterId]
     */
    suspend fun getOwnersHtml(
        year: Int,
        semesterId: String,
    ): Resource<String> {
        return processApiResource {
            httpClient.get("${BASE_URL}/$year-$semesterId/cadre/index.html")
        }
    }

    /**
     * Fetch teacher timetable in html format by [year], [semesterId] and [ownerId]
     */
    suspend fun getTimetableHtml(
        year: Int,
        semesterId: String,
        ownerId: String
    ): Resource<String> {
        return processApiResource {
            httpClient.get("${BASE_URL}/$year-$semesterId/cadre/$ownerId.html")
        }
    }

    companion object {
        private const val BASE_URL = "https://www.cs.ubbcluj.ro/files/orar"
    }
}