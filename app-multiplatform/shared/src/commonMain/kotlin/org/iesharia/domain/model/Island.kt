package org.iesharia.domain.model

/**
 * Enum para las islas del archipiélago canario
 */
enum class Island {
    TENERIFE,
    GRAN_CANARIA,
    LANZAROTE,
    FUERTEVENTURA,
    LA_PALMA,
    LA_GOMERA,
    EL_HIERRO;

    fun displayName(): String = when(this) {
        TENERIFE -> "Tenerife"
        GRAN_CANARIA -> "Gran Canaria"
        LANZAROTE -> "Lanzarote"
        FUERTEVENTURA -> "Fuerteventura"
        LA_PALMA -> "La Palma"
        LA_GOMERA -> "La Gomera"
        EL_HIERRO -> "El Hierro"
    }
}