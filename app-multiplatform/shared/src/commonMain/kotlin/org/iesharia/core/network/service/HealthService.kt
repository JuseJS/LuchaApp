package org.iesharia.core.network.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.iesharia.core.network.ApiConfig

class HealthService(private val httpClient: HttpClient) {
    
    suspend fun checkHealth(): Result<Map<String, Any>> {
        return try {
            println("🏥 Verificando conectividad con el servidor...")
            println("📍 URL: ${ApiConfig.API_BASE_URL}${ApiConfig.Endpoints.HEALTH}")
            
            val response = httpClient.get("${ApiConfig.API_BASE_URL}${ApiConfig.Endpoints.HEALTH}")
            val body = response.body<Map<String, Any>>()
            
            println("✅ Servidor accesible!")
            println("📋 Respuesta: $body")
            
            Result.success(body)
        } catch (e: Exception) {
            println("❌ Error al conectar con el servidor: ${e.message}")
            println("🔍 Tipo de error: ${e::class.simpleName}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    suspend fun testConnection(): String {
        return try {
            println("\n🔌 === PRUEBA DE CONEXIÓN ===")
            println("🌐 Base URL: ${ApiConfig.BASE_URL}")
            println("🌐 API URL: ${ApiConfig.API_BASE_URL}")
            println("🌐 Health endpoint: ${ApiConfig.API_BASE_URL}${ApiConfig.Endpoints.HEALTH}")
            
            val response = httpClient.get("${ApiConfig.API_BASE_URL}${ApiConfig.Endpoints.HEALTH}")
            val statusCode = response.status.value
            val statusDescription = response.status.description
            
            println("✅ Conexión exitosa!")
            println("📊 Estado: $statusCode - $statusDescription")
            
            val body = try {
                response.bodyAsText()
            } catch (e: Exception) {
                "Error al leer el cuerpo: ${e.message}"
            }
            
            println("📄 Respuesta del servidor: $body")
            println("=========================\n")
            
            "Conexión exitosa - Estado: $statusCode"
        } catch (e: Exception) {
            println("❌ ERROR DE CONEXIÓN")
            println("🔍 Tipo: ${e::class.simpleName}")
            println("💬 Mensaje: ${e.message}")
            println("📍 URL intentada: ${ApiConfig.API_BASE_URL}${ApiConfig.Endpoints.HEALTH}")
            e.printStackTrace()
            println("=========================\n")
            
            "Error de conexión: ${e.message}"
        }
    }
}