package org.iesharia.core.security

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Utilidad para hashear contraseñas del lado del cliente
 * Usa SHA-256 para crear un hash antes de enviar al servidor
 */
object PasswordHasher {
    
    /**
     * Genera un salt único para el cliente
     * En producción, esto debería ser único por instalación de app
     */
    fun generateClientSalt(): String {
        // En una implementación real, esto debería:
        // 1. Generarse una vez durante la instalación de la app
        // 2. Guardarse de forma segura en el dispositivo
        // 3. Ser único por dispositivo/instalación
        return "luchaapp-client-salt-2024"
    }
    
    /**
     * Hashea una contraseña con SHA-256 y un salt
     * @param password La contraseña en texto plano
     * @param email El email del usuario (usado como parte del salt)
     * @return El hash en Base64
     */
    @OptIn(ExperimentalEncodingApi::class)
    fun hashPassword(password: String, email: String): String {
        val salt = "${generateClientSalt()}-${email.lowercase()}"
        val combined = "$password$salt"
        
        // Usar SHA-256 nativo de cada plataforma
        val hash = sha256(combined.encodeToByteArray())
        
        // Convertir a Base64 para transmisión
        return Base64.encode(hash)
    }
    
    /**
     * Valida que una contraseña cumpla con los requisitos mínimos
     */
    fun validatePasswordStrength(password: String): PasswordValidationResult {
        val errors = mutableListOf<String>()
        
        if (password.length < 8) {
            errors.add("La contraseña debe tener al menos 8 caracteres")
        }
        
        if (!password.any { it.isUpperCase() }) {
            errors.add("La contraseña debe contener al menos una mayúscula")
        }
        
        if (!password.any { it.isLowerCase() }) {
            errors.add("La contraseña debe contener al menos una minúscula")
        }
        
        if (!password.any { it.isDigit() }) {
            errors.add("La contraseña debe contener al menos un número")
        }
        
        val specialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?"
        if (!password.any { it in specialChars }) {
            errors.add("La contraseña debe contener al menos un carácter especial")
        }
        
        return PasswordValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }
}

data class PasswordValidationResult(
    val isValid: Boolean,
    val errors: List<String> = emptyList()
)

/**
 * Función expect para SHA-256, implementada de forma nativa en cada plataforma
 */
expect fun sha256(data: ByteArray): ByteArray