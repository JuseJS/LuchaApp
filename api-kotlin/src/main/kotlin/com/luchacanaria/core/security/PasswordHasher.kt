package com.luchacanaria.core.security

import at.favre.lib.crypto.bcrypt.BCrypt

object PasswordHasher {
    fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return try {
            BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified
        } catch (e: Exception) {
            false
        }
    }
}
