
package org.iesharia.core.network

object ApiConfig {
    // Configuración de desarrollo
    private const val DEV_BASE_URL = "http://localhost:8080"
    
    // Configuración de producción - Dominio ASUS DDNS
    private const val PROD_BASE_URL = "http://guanxemc.asuscomm.com:8080"
    
    // Seleccionar URL según el entorno
    const val BASE_URL = PROD_BASE_URL // Usando servidor ASUS DDNS
    
    const val API_VERSION = "v1"
    const val API_BASE_URL = "$BASE_URL/api/$API_VERSION"
    
    // SSL Configuration - Not needed for HTTP
    const val ALLOW_SELF_SIGNED_SSL = false // No longer needed with HTTP
    
    // Timeouts
    const val CONNECT_TIMEOUT = 30_000L
    const val REQUEST_TIMEOUT = 60_000L
    const val SOCKET_TIMEOUT = 60_000L
    
    // Headers
    const val HEADER_AUTHORIZATION = "Authorization"
    const val HEADER_CONTENT_TYPE = "Content-Type"
    const val CONTENT_TYPE_JSON = "application/json"
    
    // JWT
    const val TOKEN_PREFIX = "Bearer "
    
    // Endpoints
    object Endpoints {
        const val AUTH_LOGIN = "/auth/login"
        const val AUTH_REGISTER = "/auth/register"
        const val WRESTLERS = "/wrestlers"
        const val TEAMS = "/teams"
        const val HEALTH = "/health"
    }
}