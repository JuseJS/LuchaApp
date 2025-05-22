package com.luchacanaria.core.config

import io.ktor.server.config.*

data class JwtConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String
)

fun ApplicationConfig.getJwtConfig(): JwtConfig {
    return JwtConfig(
        secret = propertyOrNull("jwt.secret")?.getString() ?: System.getenv("JWT_SECRET") ?: "development-secret",
        issuer = propertyOrNull("jwt.issuer")?.getString() ?: "lucha-canaria-api",
        audience = propertyOrNull("jwt.audience")?.getString() ?: "lucha-canaria-users",
        realm = propertyOrNull("jwt.realm")?.getString() ?: "lucha-canaria"
    )
}