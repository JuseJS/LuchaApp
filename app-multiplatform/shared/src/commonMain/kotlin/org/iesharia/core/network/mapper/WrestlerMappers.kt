package org.iesharia.core.network.mapper

import kotlinx.datetime.LocalDate
import org.iesharia.core.network.dto.WrestlerDto
import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.wrestlers.domain.model.WrestlerCategory
import org.iesharia.features.wrestlers.domain.model.WrestlerClassification

object WrestlerMappers {
    
    fun WrestlerDto.toDomain(): Wrestler {
        return Wrestler(
            id = this.id,
            licenseNumber = this.licenseNumber,
            name = this.name,
            surname = this.surname,
            imageUrl = this.imageUrl,
            teamId = this.teamId,
            category = WrestlerCategory.valueOf(this.category),
            classification = WrestlerClassification.valueOf(this.classification),
            height = this.height,
            weight = this.weight,
            birthDate = this.birthDate?.let { LocalDate.parse(it) },
            nickname = this.nickname
        )
    }
    
    fun Wrestler.toDto(): WrestlerDto {
        return WrestlerDto(
            id = this.id,
            licenseNumber = this.licenseNumber,
            name = this.name,
            surname = this.surname,
            imageUrl = this.imageUrl,
            teamId = this.teamId,
            category = this.category.name,
            classification = this.classification.name,
            height = this.height,
            weight = this.weight,
            birthDate = this.birthDate?.toString(),
            nickname = this.nickname
        )
    }
}