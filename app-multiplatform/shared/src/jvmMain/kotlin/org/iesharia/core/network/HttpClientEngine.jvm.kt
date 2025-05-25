package org.iesharia.core.network

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import java.util.concurrent.TimeUnit

actual fun httpClientEngine(): HttpClientEngine = OkHttp.create {
    config {
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        
        // No SSL configuration needed for HTTP
        println("✅ JVM/Desktop: Using HTTP connection")
    }
}