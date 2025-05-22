package com.luchacanaria.features.competitions.domain.model

enum class AgeCategory {
    JUVENIL,
    REGIONAL;

    fun displayName(): String = name.lowercase().replaceFirstChar { it.uppercase() }
}