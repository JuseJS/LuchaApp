package org.iesharia.config

import io.ktor.server.application.*
// Temporarily disabled due to import issues
// import io.ktor.server.plugins.calllogging.*
// import io.ktor.server.request.*
// import io.ktor.server.response.*
// import org.slf4j.event.Level

fun Application.configureLogging() {
    // Temporarily disabled CallLogging due to import issues
    // Will use console logging instead
    log.info("Logging configured - using console output")
    
    /*
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/api") }
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            val path = call.request.path()
            "Status: $status, HTTP method: $httpMethod, Path: $path, User agent: $userAgent"
        }
    }
    */
}