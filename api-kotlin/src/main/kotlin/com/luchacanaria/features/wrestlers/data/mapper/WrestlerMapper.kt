package com.luchacanaria.features.wrestlers.data.mapper

import com.luchacanaria.features.wrestlers.data.document.WrestlerDocument
import com.luchacanaria.features.wrestlers.domain.model.Wrestler
import com.luchacanaria.features.wrestlers.domain.model.WrestlerCategory
import com.luchacanaria.features.wrestlers.domain.model.WrestlerClassification
import kotlinx.datetime.LocalDate

object WrestlerMapper {
    fun toDomain(document: WrestlerDocument): Wrestler {
        return Wrestler(
            id = document.id,
            licenseNumber = document.licenseNumber,
            name = document.name,
            surname = document.surname,
            imageUrl = document.imageUrl,
            teamId = document.teamId,
            category = WrestlerCategory.valueOf(document.category),
            classification = WrestlerClassification.valueOf(document.classification),
            height = document.height,
            weight = document.weight,
            birthDate = document.birthDate?.let { LocalDate.parse(it) },
            nickname = document.nickname
        )
    }

    fun toDocument(wrestler: Wrestler): WrestlerDocument {
        return WrestlerDocument(
            licenseNumber = wrestler.licenseNumber,
            name = wrestler.name,
            surname = wrestler.surname,
            imageUrl = wrestler.imageUrl,
            teamId = wrestler.teamId,
            category = wrestler.category.name,
            classification = wrestler.classification.name,
            height = wrestler.height,
            weight = wrestler.weight,
            birthDate = wrestler.birthDate?.toString(),
            nickname = wrestler.nickname
        )
    }
}