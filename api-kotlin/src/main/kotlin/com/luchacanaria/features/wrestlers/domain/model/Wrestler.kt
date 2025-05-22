package com.luchacanaria.features.wrestlers.domain.model

import kotlinx.datetime.LocalDate

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
    val nickname: String?
) {
    val fullName: String get() = "$name $surname"
}