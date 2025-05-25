package org.iesharia.core.security

import kotlinx.cinterop.*
import platform.CoreCrypto.*

/**
 * Implementación iOS de SHA-256
 */
@OptIn(ExperimentalForeignApi::class)
actual fun sha256(data: ByteArray): ByteArray {
    val hash = UByteArray(CC_SHA256_DIGEST_LENGTH)
    
    data.usePinned { pinned ->
        CC_SHA256(pinned.addressOf(0), data.size.toUInt(), hash.refTo(0))
    }
    
    return hash.toByteArray()
}