package org.iesharia.config

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureCORS() {
    install(CORS) {
        // Agregar hosts de forma explícita
        allowHost("localhost", listOf("http"), subDomains = listOf("3000", "8081"))
        allowHost("guanxemc.asuscomm.com", listOf("http", "https"))
        allowHost("guanxemc.asuscomm.com", listOf("http", "https"), subDomains = listOf("8080"))

        // Métodos HTTP permitidos
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)

        // Headers permitidos
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader("X-Requested-With")

        // Permitir credenciales
        allowCredentials = true

        // Headers expuestos
        exposeHeader(HttpHeaders.ContentLength)
        exposeHeader(HttpHeaders.ContentType)

        // Tiempo máximo de cache para preflight
        maxAgeInSeconds = 3600
    }
}