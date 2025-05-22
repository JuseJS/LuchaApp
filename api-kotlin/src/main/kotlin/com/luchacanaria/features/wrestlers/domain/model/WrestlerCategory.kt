package com.luchacanaria.features.wrestlers.domain.model

enum class WrestlerCategory {
    REGIONAL,
    JUVENIL,
    CADETE;

    fun displayName(): String = name.lowercase().replaceFirstChar { it.uppercase() }
}