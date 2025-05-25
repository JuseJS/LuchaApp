package org.iesharia.core.security

/**
 * iOS implementation of PasswordEncoderFactory
 */
actual object PasswordEncoderFactory {
    actual fun getEncoder(): PasswordEncoder = NoOpPasswordEncoder()
}