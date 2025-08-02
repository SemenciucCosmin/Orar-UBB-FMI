package com.ubb.fmi.orar.data.rooms.api

import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.processApiResource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class RoomsApi(private val httpClient: HttpClient) {
    suspend fun getRoomsMapHtml(
        year: Int,
        semester: String,
    ): Resource<String> {
        return processApiResource {
            httpClient.get("$BASE_URL/$year-$semester/sali/legenda.html")
        }
    }

    suspend fun getRoomTimetableHtml(
        year: Int,
        semester: String,
        roomId: String
    ): Resource<String> {
        return processApiResource {
            httpClient.get("$BASE_URL/$year-$semester/sali/$roomId.html")
        }
    }

    companion object {
        private const val BASE_URL = "https://www.cs.ubbcluj.ro/files/orar"
    }
}
