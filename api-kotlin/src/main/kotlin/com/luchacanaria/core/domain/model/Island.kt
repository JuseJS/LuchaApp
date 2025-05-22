package com.luchacanaria.core.domain.model

enum class Island {
    TENERIFE,
    GRAN_CANARIA,
    LA_PALMA,
    LA_GOMERA,
    EL_HIERRO,
    LANZAROTE,
    FUERTEVENTURA;

    fun displayName(): String = when (this) {
        TENERIFE -> "Tenerife"
        GRAN_CANARIA -> "Gran Canaria"
        LA_PALMA -> "La Palma"
        LA_GOMERA -> "La Gomera"
        EL_HIERRO -> "El Hierro"
        LANZAROTE -> "Lanzarote"
        FUERTEVENTURA -> "Fuerteventura"
    }
}