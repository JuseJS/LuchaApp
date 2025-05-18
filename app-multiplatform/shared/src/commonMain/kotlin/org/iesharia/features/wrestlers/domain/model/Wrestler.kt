package org.iesharia.features.wrestlers.domain.model

import kotlinx.datetime.LocalDate

/**
 * Categorías de edad para luchadores
 */
enum class WrestlerCategory {
    REGIONAL,
    JUVENIL,
    CADETE;

    fun displayName(): String = name.lowercase().replaceFirstChar { it.uppercase() }
}

/**
 * Clasificación de los luchadores
 */
enum class WrestlerClassification {
    PUNTAL_A,
    PUNTAL_B,
    PUNTAL_C,
    DESTACADO_A,
    DESTACADO_B,
    DESTACADO_C,
    NONE;

    fun displayName(): String = when(this) {
        PUNTAL_A -> "Puntal A"
        PUNTAL_B -> "Puntal B"
        PUNTAL_C -> "Puntal C"
        DESTACADO_A -> "Destacado A"
        DESTACADO_B -> "Destacado B"
        DESTACADO_C -> "Destacado C"
        NONE -> "No Clasificado"
    }

    companion object {
        fun getOrderedValues(): List<WrestlerClassification> {
            return listOf(
                PUNTAL_A, PUNTAL_B, PUNTAL_C,
                DESTACADO_A, DESTACADO_B, DESTACADO_C,
                NONE
            )
        }
    }
}

/**
 * Modelo que representa a un luchador
 */
data class Wrestler(
    val id: String,
    val licenseNumber: String,
    val name: String,
    val surname: String,
    val imageUrl: String?,
    val teamId: String,
    val category: WrestlerCategory,
    val classification: WrestlerClassification,
    val height: Int?,
    val weight: Int?,
    val birthDate: LocalDate?,
    val nickname: String?,
) {
    val fullName: String get() = "$name $surname"
}