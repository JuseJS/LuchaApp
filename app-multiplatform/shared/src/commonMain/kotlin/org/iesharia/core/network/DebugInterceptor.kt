package org.iesharia.core.network

import io.ktor.client.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.statement.*
import io.ktor.http.*

// Extension para agregar logging de depuración
fun HttpClientConfig<*>.installDebugInterceptor() {
    // Usar el ResponseObserver built-in de Ktor para logging
    install(ResponseObserver) {
        onResponse { response ->
            val request = response.request
            val separator = "=".repeat(60)
            
            println("\n$separator")
            println("🚀 REQUEST: ${request.method.value} ${request.url}")
            println(separator)
            
            // Log headers
            println("\n📋 REQUEST HEADERS:")
            request.headers.entries().forEach { (key, values) ->
                if (key != HttpHeaders.Authorization) {
                    println("  $key: ${values.joinToString(", ")}")
                } else {
                    println("  $key: [REDACTED]")
                }
            }
            
            println("\n$separator")
            println("✅ RESPONSE: ${request.method.value} ${request.url}")
            println(separator)
            
            println("\n📊 STATUS: ${response.status.value} ${response.status.description}")
            
            // Log response headers
            println("\n📋 RESPONSE HEADERS:")
            response.headers.entries().forEach { (key, values) ->
                println("  $key: ${values.joinToString(", ")}")
            }
            
            if (!response.status.isSuccess()) {
                val errorSeparator = "🚨".repeat(30)
                println("\n$errorSeparator")
                println("❌ ERROR RESPONSE")
                println(errorSeparator)
            }
            
            println("$separator\n")
        }
    }
}