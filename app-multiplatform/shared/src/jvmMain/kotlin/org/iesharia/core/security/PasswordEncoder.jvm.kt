package org.iesharia.core.security

/**
 * JVM implementation of PasswordEncoderFactory
 */
actual object PasswordEncoderFactory {
    actual fun getEncoder(): PasswordEncoder = NoOpPasswordEncoder()
}