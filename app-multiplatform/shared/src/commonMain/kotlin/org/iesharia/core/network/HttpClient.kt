package org.iesharia.core.network

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

expect fun httpClientEngine(): HttpClientEngine

object HttpClientProvider {
    
    fun createHttpClient(tokenStorage: TokenStorage): HttpClient {
        println("🔧 Creando HttpClient con URL base: ${ApiConfig.API_BASE_URL}")
        
        return HttpClient(httpClientEngine()) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("🌐 HTTP Client: $message")
                    }
                }
                level = LogLevel.ALL
                
                sanitizeHeader { header -> 
                    header == HttpHeaders.Authorization 
                }
            }
            
            install(HttpTimeout) {
                requestTimeoutMillis = ApiConfig.REQUEST_TIMEOUT
                connectTimeoutMillis = ApiConfig.CONNECT_TIMEOUT
                socketTimeoutMillis = ApiConfig.SOCKET_TIMEOUT
            }
            
            // Comentamos el plugin Auth por ahora
            /*install(Auth) {
                bearer {
                    loadTokens {
                        tokenStorage.getToken()?.let {
                            BearerTokens(
                                accessToken = it,
                                refreshToken = null
                            )
                        }
                    }
                    
                    // Send bearer token on 401
                    refreshTokens {
                        tokenStorage.getToken()?.let {
                            BearerTokens(
                                accessToken = it,
                                refreshToken = null
                            )
                        }
                    }
                }
            }*/
            
            install(DefaultRequest) {
                headers {
                    append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    append(HttpHeaders.Accept, ContentType.Application.Json.toString())
                }
            }
            
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 3)
                exponentialDelay()
            }
            
            // Interceptor simple para agregar el token
            install(HttpCallValidator) {
                validateResponse { response ->
                    // No hacer nada aquí, solo lo instalamos para el interceptor
                }
            }
            
            // Usar el defaultRequest para agregar el token
            defaultRequest {
                tokenStorage.getToken()?.let { token ->
                    println("🔑 Token disponible: ${token.take(20)}...")
                    header(HttpHeaders.Authorization, "Bearer $token")
                } ?: println("⚠️ No hay token disponible")
            }
            
            // Instalar interceptor de depuración
            installDebugInterceptor()
        }
    }
}