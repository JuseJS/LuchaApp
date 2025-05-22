package com.luchacanaria.features.competitions.domain.model

enum class DivisionCategory {
    PRIMERA,
    SEGUNDA,
    TERCERA;

    fun displayName(): String = name.lowercase().replaceFirstChar { it.uppercase() }
}