package com.luchacanaria.core.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.luchacanaria.core.config.getJwtConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    val jwtConfig = environment.config.getJwtConfig()
    val jwtSecret = jwtConfig.secret
    val jwtIssuer = jwtConfig.issuer
    val jwtAudience = jwtConfig.audience

    install(Authentication) {
        jwt("auth-jwt") {
            realm = jwtConfig.realm
            verifier(
                JWT.require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtIssuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("userId").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}

object JWTService {
    private val jwtSecret = System.getenv("JWT_SECRET") ?: "lucha-canaria-secret-key-change-in-production"
    private val jwtIssuer = "lucha-canaria-api"
    private val jwtAudience = "lucha-canaria-users"
    private val algorithm = Algorithm.HMAC256(jwtSecret)

    fun generateToken(userId: String, email: String, role: String): String {
        return JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtIssuer)
            .withClaim("userId", userId)
            .withClaim("email", email)
            .withClaim("role", role)
            .withExpiresAt(java.util.Date(System.currentTimeMillis() + 60000 * 60 * 24)) // 24 horas
            .sign(algorithm)
    }
}
