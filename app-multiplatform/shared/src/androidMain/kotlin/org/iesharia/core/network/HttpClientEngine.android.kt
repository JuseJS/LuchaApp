package org.iesharia.core.network

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*

actual fun httpClientEngine(): HttpClientEngine = OkHttp.create {
    config {
        // No SSL configuration needed for HTTP
        println("✅ Android: Using HTTP connection")
    }
}