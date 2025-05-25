package org.iesharia.core.security

/**
 * Android implementation of PasswordEncoderFactory
 */
actual object PasswordEncoderFactory {
    actual fun getEncoder(): PasswordEncoder = NoOpPasswordEncoder()
}