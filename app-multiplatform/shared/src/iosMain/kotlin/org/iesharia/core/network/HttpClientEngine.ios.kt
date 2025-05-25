package org.iesharia.core.network

import io.ktor.client.engine.*
import io.ktor.client.engine.darwin.*

actual fun httpClientEngine(): HttpClientEngine = Darwin.create {
    configureRequest {
        setAllowsCellularAccess(true)
        // No SSL configuration needed for HTTP
        println("✅ iOS: Using HTTP connection")
    }
}