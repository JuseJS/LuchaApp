package org.iesharia.core.network.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.iesharia.core.network.ApiConfig
import org.iesharia.core.network.dto.LoginRequest
import org.iesharia.core.network.dto.LoginResponse
import org.iesharia.core.network.dto.RegisterRequest
import org.iesharia.core.network.dto.UserDto
import org.iesharia.core.security.PasswordHasher
import org.iesharia.core.network.TokenStorage

class AuthService(
    private val httpClient: HttpClient,
    private val tokenStorage: TokenStorage
) {
    
    init {
        println("🔐 AuthService inicializado")
        println("📍 Base URL: ${ApiConfig.API_BASE_URL}")
        println("📍 Login endpoint: ${ApiConfig.Endpoints.AUTH_LOGIN}")
    }
    
    suspend fun login(email: String, password: String): LoginResponse {
        println("\n🔑 Iniciando login...")
        println("📧 Email: $email")
        println("🔐 Hasheando contraseña con SHA-256...")
        
        // Hashear la contraseña antes de enviarla
        val hashedPassword = PasswordHasher.hashPassword(password, email)
        println("✅ Contraseña hasheada (primeros 10 chars): ${hashedPassword.take(10)}...")
        println("🔗 URL completa: ${ApiConfig.API_BASE_URL}${ApiConfig.Endpoints.AUTH_LOGIN}")
        
        return try {
            val response = httpClient.post("${ApiConfig.API_BASE_URL}${ApiConfig.Endpoints.AUTH_LOGIN}") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, hashedPassword))
            }.body<LoginResponse>()
            
            println("✅ Login exitoso!")
            println("👤 Usuario: ${response.user.email}")
            println("🎫 Token recibido: ${if (response.token.isNotEmpty()) "Sí" else "No"}")
            
            response
        } catch (e: Exception) {
            println("❌ Error en login: ${e.message}")
            println("🔍 Tipo de error: ${e::class.simpleName}")
            e.printStackTrace()
            throw e
        }
    }
    
    suspend fun register(
        name: String,
        surname: String,
        email: String,
        password: String
    ): UserDto {
        println("\n📝 Iniciando registro...")
        println("📧 Email: $email")
        
        // Validar la contraseña antes de enviarla
        val validation = PasswordHasher.validatePasswordStrength(password)
        if (!validation.isValid) {
            println("❌ Contraseña no válida: ${validation.errors.joinToString(", ")}")
            throw IllegalArgumentException(validation.errors.first())
        }
        
        println("🔐 Hasheando contraseña con SHA-256...")
        val hashedPassword = PasswordHasher.hashPassword(password, email)
        println("✅ Contraseña hasheada y validada")
        println("🔗 URL completa: ${ApiConfig.API_BASE_URL}${ApiConfig.Endpoints.AUTH_REGISTER}")
        
        return try {
            val httpResponse = httpClient.post("${ApiConfig.API_BASE_URL}${ApiConfig.Endpoints.AUTH_REGISTER}") {
                contentType(ContentType.Application.Json)
                setBody(
                    RegisterRequest(
                        email = email,
                        password = hashedPassword,
                        name = name,
                        surname = surname
                    )
                )
            }
            
            when (httpResponse.status) {
                HttpStatusCode.Created, HttpStatusCode.OK -> {
                    val response = httpResponse.body<LoginResponse>()
                    println("✅ Registro exitoso!")
                    println("👤 Usuario creado: ${response.user.email}")
                    println("🎫 Token recibido: ${if (response.token.isNotEmpty()) "Sí" else "No"}")
                    
                    // Store the token after successful registration
                    tokenStorage.saveToken(response.token)
                    
                    response.user
                }
                HttpStatusCode.BadRequest -> {
                    val errorBody = try {
                        httpResponse.bodyAsText()
                    } catch (e: Exception) {
                        "Could not read error body"
                    }
                    println("❌ Error 400: $errorBody")
                    throw Exception("Bad Request: $errorBody")
                }
                HttpStatusCode.Conflict -> {
                    println("❌ Error 409: Usuario ya existe")
                    throw Exception("User already exists")
                }
                else -> {
                    val errorBody = try {
                        httpResponse.bodyAsText()
                    } catch (e: Exception) {
                        "Could not read error body"
                    }
                    println("❌ Error ${httpResponse.status}: $errorBody")
                    throw Exception("Registration failed: ${httpResponse.status}")
                }
            }
        } catch (e: Exception) {
            println("❌ Error en registro: ${e.message}")
            println("🔍 Tipo de error: ${e::class.simpleName}")
            e.printStackTrace()
            throw e
        }
    }
    
    suspend fun healthCheck(): Map<String, Any> {
        println("\n🏥 Realizando health check...")
        println("🔗 URL: ${ApiConfig.API_BASE_URL}${ApiConfig.Endpoints.HEALTH}")
        
        return try {
            val response = httpClient.get("${ApiConfig.API_BASE_URL}${ApiConfig.Endpoints.HEALTH}").body<Map<String, Any>>()
            println("✅ Health check exitoso!")
            println("📋 Respuesta: $response")
            response
        } catch (e: Exception) {
            println("❌ Error en health check: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}