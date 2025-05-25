package org.iesharia

import io.ktor.server.application.*
import org.iesharia.config.EnvironmentConfig
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.iesharia.config.configureAuthentication
import org.iesharia.config.configureCORS
import org.iesharia.config.configureDependencyInjection
import org.iesharia.config.configureLogging
import org.iesharia.config.configureRouting
import org.iesharia.config.configureSerialization
import org.iesharia.config.configureStatusPages

fun main() {
    val host = EnvironmentConfig.getEnv("API_HOST", "0.0.0.0")!!
    val port = EnvironmentConfig.getEnv("PORT", "8080")!!.toIntOrNull() ?: 8080
    
    embeddedServer(Netty, port = port, host = host) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    configureDependencyInjection()
    configureLogging()
    configureSerialization()
    configureAuthentication()
    configureCORS()
    configureStatusPages()
    configureRouting()
}