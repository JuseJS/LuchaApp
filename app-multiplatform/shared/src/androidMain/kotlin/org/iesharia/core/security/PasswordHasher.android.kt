package org.iesharia.core.security

import java.security.MessageDigest

/**
 * Implementación Android de SHA-256
 */
actual fun sha256(data: ByteArray): ByteArray {
    val md = MessageDigest.getInstance("SHA-256")
    return md.digest(data)
}