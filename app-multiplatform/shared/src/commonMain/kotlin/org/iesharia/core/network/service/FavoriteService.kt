package org.iesharia.core.network.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.iesharia.core.network.ApiConfig
import org.iesharia.core.network.dto.*

class FavoriteService(
    private val httpClient: HttpClient
) {
    suspend fun getFavorites(type: EntityTypeDto? = null): List<FavoriteResponseDto> {
        val url = if (type != null) {
            "${ApiConfig.API_BASE_URL}/favorites?type=$type"
        } else {
            "${ApiConfig.API_BASE_URL}/favorites"
        }
        
        val response = httpClient.get(url)
        
        // Check if response is successful
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            // Return empty list on error (401, etc)
            return emptyList()
        }
    }
    
    suspend fun addFavorite(entityId: String, entityType: EntityTypeDto): FavoriteResponseDto {
        return httpClient.post("${ApiConfig.API_BASE_URL}/favorites") {
            contentType(ContentType.Application.Json)
            setBody(FavoriteRequestDto(entityId, entityType))
        }.body()
    }
    
    suspend fun removeFavorite(entityId: String, entityType: EntityTypeDto) {
        httpClient.delete("${ApiConfig.API_BASE_URL}/favorites/${entityType}/${entityId}")
    }
    
    suspend fun checkFavorite(entityId: String, entityType: EntityTypeDto): Boolean {
        return try {
            val response = httpClient.get(
                "${ApiConfig.API_BASE_URL}/favorites/${entityType}/${entityId}/status"
            )
            if (response.status.isSuccess()) {
                val status: FavoriteStatusDto = response.body()
                status.isFavorite
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}