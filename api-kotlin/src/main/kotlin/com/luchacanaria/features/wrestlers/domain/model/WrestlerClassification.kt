package com.luchacanaria.features.wrestlers.domain.model

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