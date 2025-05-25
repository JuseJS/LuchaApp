package org.iesharia.core.network.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.iesharia.core.network.ApiConfig
import org.iesharia.core.network.dto.CreateWrestlerRequest
import org.iesharia.core.network.dto.UpdateWrestlerRequest
import org.iesharia.core.network.dto.WrestlerDto

class WrestlerService(private val httpClient: HttpClient) {
    
    suspend fun getAllWrestlers(
        teamId: String? = null,
        category: String? = null,
        classification: String? = null,
        island: String? = null,
        search: String? = null,
        page: Int = 0,
        size: Int = 20
    ): List<WrestlerDto> {
        return httpClient.get("${ApiConfig.API_BASE_URL}${ApiConfig.Endpoints.WRESTLERS}") {
            teamId?.let { parameter("teamId", it) }
            category?.let { parameter("category", it) }
            classification?.let { parameter("classification", it) }
            island?.let { parameter("island", it) }
            search?.let { parameter("search", it) }
            parameter("page", page)
            parameter("size", size)
        }.body()
    }
    
    suspend fun getWrestlerById(id: String): WrestlerDto {
        return httpClient.get("${ApiConfig.API_BASE_URL}${ApiConfig.Endpoints.WRESTLERS}/$id").body()
    }
    
    suspend fun getWrestlersByTeamId(teamId: String): List<WrestlerDto> {
        return httpClient.get("${ApiConfig.API_BASE_URL}${ApiConfig.Endpoints.WRESTLERS}/team/$teamId").body()
    }
    
    suspend fun createWrestler(request: CreateWrestlerRequest): WrestlerDto {
        return httpClient.post("${ApiConfig.API_BASE_URL}${ApiConfig.Endpoints.WRESTLERS}") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
    
    suspend fun updateWrestler(id: String, request: UpdateWrestlerRequest): WrestlerDto {
        return httpClient.put("${ApiConfig.API_BASE_URL}${ApiConfig.Endpoints.WRESTLERS}/$id") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
    
    suspend fun deleteWrestler(id: String): Boolean {
        val response = httpClient.delete("${ApiConfig.API_BASE_URL}${ApiConfig.Endpoints.WRESTLERS}/$id")
        return response.status.isSuccess()
    }
}