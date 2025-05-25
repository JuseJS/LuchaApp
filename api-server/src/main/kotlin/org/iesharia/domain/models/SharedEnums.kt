package org.iesharia.domain.models

import kotlinx.serialization.Serializable

@Serializable
enum class Island {
    TENERIFE,
    GRAN_CANARIA,
    LANZAROTE,
    FUERTEVENTURA,
    LA_PALMA,
    LA_GOMERA,
    EL_HIERRO
}

@Serializable
enum class DivisionCategory {
    PRIMERA,
    SEGUNDA,
    TERCERA
}

@Serializable
enum class AgeCategory {
    JUVENIL,
    REGIONAL
}

@Serializable
enum class WrestlerCategory {
    REGIONAL,
    JUVENIL,
    CADETE
}

@Serializable
enum class WrestlerClassification {
    PUNTAL_A,
    PUNTAL_B,
    PUNTAL_C,
    DESTACADO_A,
    DESTACADO_B,
    DESTACADO_C,
    NONE
}

@Serializable
enum class UserRole {
    GUEST,
    REGISTERED_USER,
    COACH,
    FEDERATIVE_DELEGATE
}

@Serializable
enum class MatchStatus {
    SCHEDULED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

@Serializable
enum class MatchResult {
    WIN,
    LOSS,
    DRAW
}