package org.iesharia.core.network.service

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.iesharia.core.network.ApiConfig

class TestAuthService(
    private val httpClient: HttpClient
) {
    suspend fun testAuthenticatedEndpoint(): String {
        return try {
            val response = httpClient.get("${ApiConfig.API_BASE_URL}/users/me")
            "Status: ${response.status}, Body: ${response.bodyAsText()}"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}