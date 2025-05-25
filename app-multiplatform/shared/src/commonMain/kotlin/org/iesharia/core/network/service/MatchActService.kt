package org.iesharia.core.network.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.iesharia.core.network.ApiConfig
import org.iesharia.core.network.dto.MatchActDto
import org.iesharia.core.network.dto.RefereeDto

class MatchActService(
    private val httpClient: HttpClient
) {
    
    suspend fun getMatchAct(actId: String): MatchActDto {
        return httpClient.get("${ApiConfig.API_BASE_URL}/matchacts/$actId").body()
    }
    
    suspend fun getMatchActByMatchId(matchId: String): MatchActDto? {
        return try {
            val response = httpClient.get("${ApiConfig.API_BASE_URL}/matchacts/match/$matchId")
            if (response.status == HttpStatusCode.NotFound) {
                println("MatchActService: No se encontró acta para matchId: $matchId")
                return null
            }
            response.body()
        } catch (e: Exception) {
            println("MatchActService: Error al obtener acta para matchId $matchId: ${e.message}")
            null
        }
    }
    
    suspend fun saveMatchAct(matchAct: MatchActDto): MatchActDto {
        println("MatchActService.saveMatchAct - Enviando POST con ID: ${matchAct.id}, MatchId: ${matchAct.matchId}")
        val response = httpClient.post("${ApiConfig.API_BASE_URL}/matchacts") {
            contentType(ContentType.Application.Json)
            setBody(matchAct)
        }
        
        // Check if the response is successful
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            // Try to parse error message
            val errorBody = response.bodyAsText()
            val errorMessage = try {
                val json = Json.parseToJsonElement(errorBody)
                json.jsonObject["error"]?.jsonPrimitive?.content ?: "Error desconocido"
            } catch (e: Exception) {
                errorBody
            }
            
            // Check for duplicate key error
            if (errorMessage.contains("duplicate key error") && errorMessage.contains("matchId")) {
                throw Exception("Ya existe un acta para este enfrentamiento. Use la opción de actualizar.")
            }
            
            throw Exception(errorMessage)
        }
    }
    
    suspend fun updateMatchAct(actId: String, matchAct: MatchActDto): MatchActDto {
        println("MatchActService.updateMatchAct - Enviando PUT para actId: $actId, con ID en DTO: ${matchAct.id}, MatchId: ${matchAct.matchId}")
        val response = httpClient.put("${ApiConfig.API_BASE_URL}/matchacts/$actId") {
            contentType(ContentType.Application.Json)
            setBody(matchAct)
        }
        
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            val errorBody = response.bodyAsText()
            val errorMessage = try {
                val json = Json.parseToJsonElement(errorBody)
                json.jsonObject["error"]?.jsonPrimitive?.content ?: "Error desconocido"
            } catch (e: Exception) {
                errorBody
            }
            throw Exception(errorMessage)
        }
    }
    
    suspend fun completeMatchAct(actId: String): MatchActDto {
        val response = httpClient.post("${ApiConfig.API_BASE_URL}/matchacts/$actId/complete")
        
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            val errorBody = response.bodyAsText()
            val errorMessage = try {
                val json = Json.parseToJsonElement(errorBody)
                json.jsonObject["error"]?.jsonPrimitive?.content ?: "Error desconocido"
            } catch (e: Exception) {
                errorBody
            }
            throw Exception(errorMessage)
        }
    }
    
    suspend fun getAvailableReferees(): List<RefereeDto> {
        return httpClient.get("${ApiConfig.API_BASE_URL}/referees/available").body()
    }
}