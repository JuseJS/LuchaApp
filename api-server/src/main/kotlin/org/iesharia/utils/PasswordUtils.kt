package org.iesharia.utils

import at.favre.lib.crypto.bcrypt.BCrypt
import java.util.Base64

object PasswordUtils {
    private const val BCRYPT_COST = 12
    
    /**
     * Hashea una contraseña que ya viene pre-hasheada con SHA-256 desde el cliente
     * @param preHashedPassword Contraseña ya hasheada con SHA-256 en Base64
     * @return Hash BCrypt del hash SHA-256
     */
    fun hashPassword(preHashedPassword: String): String {
        // La contraseña ya viene hasheada con SHA-256 desde el cliente
        // Aplicamos BCrypt sobre ese hash para doble seguridad
        return BCrypt.withDefaults().hashToString(BCRYPT_COST, preHashedPassword.toCharArray())
    }
    
    /**
     * Verifica una contraseña pre-hasheada contra el hash BCrypt almacenado
     * @param preHashedPassword Contraseña ya hasheada con SHA-256 en Base64
     * @param storedBcryptHash Hash BCrypt almacenado en la base de datos
     * @return true si coinciden
     */
    fun verifyPassword(preHashedPassword: String, storedBcryptHash: String): Boolean {
        // La contraseña viene pre-hasheada con SHA-256 desde el cliente
        // Verificamos ese hash contra el BCrypt almacenado
        return BCrypt.verifyer().verify(preHashedPassword.toCharArray(), storedBcryptHash).verified
    }
    
    /**
     * Valida que el hash SHA-256 tenga el formato esperado
     * La validación de fuerza de contraseña se hace en el cliente
     * @param preHashedPassword Hash SHA-256 en Base64
     * @return Par con (esVálido, mensaje de error)
     */
    fun validatePasswordStrength(preHashedPassword: String): Pair<Boolean, String?> {
        return when {
            // Un hash SHA-256 en Base64 debe tener 44 caracteres
            preHashedPassword.length != 44 -> false to "Invalid password hash format"
            // Debe contener solo caracteres Base64 válidos
            !preHashedPassword.matches(Regex("^[A-Za-z0-9+/=]+$")) -> false to "Invalid password hash characters"
            else -> true to null
        }
    }
    
    /**
     * Detecta si una contraseña está en formato antiguo (texto plano) o nuevo (SHA-256)
     * Esto es útil durante la migración
     */
    fun isPreHashedPassword(password: String): Boolean {
        // Un hash SHA-256 en Base64 siempre tiene 44 caracteres y termina con =
        return password.length == 44 && 
               password.matches(Regex("^[A-Za-z0-9+/=]+$")) &&
               password.endsWith("=")
    }
    
    /**
     * Método de compatibilidad para migrar usuarios existentes
     * Si detecta una contraseña en texto plano, la hashea directamente
     * Si detecta un hash SHA-256, aplica BCrypt sobre el hash
     */
    fun hashPasswordCompatible(password: String): String {
        return if (isPreHashedPassword(password)) {
            // Nueva forma: BCrypt sobre SHA-256
            hashPassword(password)
        } else {
            // Forma antigua: BCrypt directo (solo para migración)
            BCrypt.withDefaults().hashToString(BCRYPT_COST, password.toCharArray())
        }
    }
    
    /**
     * Verifica contraseñas con compatibilidad hacia atrás
     */
    fun verifyPasswordCompatible(password: String, storedHash: String): Boolean {
        return if (isPreHashedPassword(password)) {
            // Nueva forma: verificar SHA-256 contra BCrypt
            verifyPassword(password, storedHash)
        } else {
            // Forma antigua: verificar texto plano contra BCrypt
            BCrypt.verifyer().verify(password.toCharArray(), storedHash).verified
        }
    }
}