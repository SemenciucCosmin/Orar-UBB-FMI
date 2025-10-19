package com.ubb.fmi.orar.data.network.service

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.processApiResource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/**
 * Api service for fetching room related information
 */
class RoomsApi(private val httpClient: HttpClient) {

    /**
     * Fetch rooms table in html format by [year] and [semesterId]
     */
    suspend fun getRoomsHtml(
        year: Int,
        semesterId: String,
    ): Resource<String> {
        return processApiResource {
            httpClient.get("$BASE_URL/$year-$semesterId/sali/legenda.html")
        }
    }

    /**
     * Fetch room events in html format by [year], [semesterId] and [roomsId]
     */
    suspend fun getEventsHtml(
        year: Int,
        semesterId: String,
        roomsId: String
    ): Resource<String> {
        return processApiResource {
            httpClient.get("$BASE_URL/$year-$semesterId/sali/$roomsId.html")
        }
    }

    companion object {
        private const val BASE_URL = "https://www.cs.ubbcluj.ro/files/orar"
    }
}