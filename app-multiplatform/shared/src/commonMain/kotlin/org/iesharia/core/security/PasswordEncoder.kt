package org.iesharia.core.security

/**
 * Interface for password encoding across platforms
 */
interface PasswordEncoder {
    fun encode(password: String): String
    fun matches(password: String, encodedPassword: String): Boolean
}

/**
 * Default implementation that does no encoding (for development/testing)
 * In production, use platform-specific implementations
 */
class NoOpPasswordEncoder : PasswordEncoder {
    override fun encode(password: String): String = password
    override fun matches(password: String, encodedPassword: String): Boolean = 
        password == encodedPassword
}

/**
 * Factory for getting the appropriate password encoder
 */
expect object PasswordEncoderFactory {
    fun getEncoder(): PasswordEncoder
}