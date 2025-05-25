package org.iesharia.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import kotlinx.datetime.Clock
import org.iesharia.config.EnvironmentConfig
import kotlinx.datetime.toJavaInstant
import org.iesharia.data.models.UserDocument
import kotlin.time.Duration.Companion.hours

class JwtManager {
    private val secret = EnvironmentConfig.requireEnv("JWT_SECRET")
    private val issuer = EnvironmentConfig.getEnv("JWT_ISSUER", "luchaapp-api")!!
    private val audience = EnvironmentConfig.getEnv("JWT_AUDIENCE", "luchaapp-client")!!
    private val expirationHours = EnvironmentConfig.getEnv("JWT_EXPIRATION_HOURS")?.toLongOrNull() ?: 24L
    
    private val algorithm = Algorithm.HMAC256(secret)
    val verifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()
    
    fun generateToken(user: UserDocument): String {
        val now = Clock.System.now()
        val expiration = now.plus(expirationHours.hours)
        
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withSubject(user.id)
            .withClaim("userId", user.id) // Agregamos también como claim para compatibilidad
            .withClaim("email", user.email)
            .withClaim("role", user.role.name)
            .withClaim("permissions", user.permissions)
            .withClaim("teamId", user.associatedTeamId)
            .withIssuedAt(now.toJavaInstant())
            .withExpiresAt(expiration.toJavaInstant())
            .sign(algorithm)
    }
    
    fun verifyToken(token: String): DecodedJWT? {
        return try {
            verifier.verify(token)
        } catch (e: JWTVerificationException) {
            null
        }
    }
    
    fun getUserId(token: String): String? {
        return verifyToken(token)?.subject
    }
    
    fun getExpirationTime(): Long {
        return expirationHours * 3600 // Convert to seconds
    }
}