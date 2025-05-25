package org.iesharia.features.matches.data.repository

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.iesharia.core.domain.model.AppError
import org.iesharia.core.network.dto.*
import org.iesharia.core.network.service.MatchActService
import org.iesharia.features.competitions.domain.model.AgeCategory
import org.iesharia.features.matches.domain.model.*
import org.iesharia.features.matches.domain.repository.MatchActRepository

class HttpMatchActRepository(
    private val matchActService: MatchActService
) : MatchActRepository {
    
    override suspend fun getMatchAct(actId: String): MatchAct? {
        return try {
            val dto = matchActService.getMatchAct(actId)
            dto.toDomain()
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun getMatchActByMatchId(matchId: String): MatchAct? {
        return try {
            val dto = matchActService.getMatchActByMatchId(matchId)
            println("HttpMatchActRepository.getMatchActByMatchId - DTO received: ${dto?.id}")
            dto?.toDomain()?.also { act ->
                println("HttpMatchActRepository.getMatchActByMatchId - MatchAct domain: ${act.id}")
            }
        } catch (e: Exception) {
            println("HttpMatchActRepository.getMatchActByMatchId - Error: ${e.message}")
            null
        }
    }
    
    override suspend fun saveMatchAct(matchAct: MatchAct): MatchAct {
        return try {
            println("=== HttpMatchActRepository.saveMatchAct ===")
            println("MatchAct ID recibido: '${matchAct.id}'")
            println("MatchAct matchId: ${matchAct.matchId}")
            println("¿ID empieza con 'temp_'?: ${matchAct.id.startsWith("temp_")}")
            println("¿ID está vacío?: ${matchAct.id.isEmpty()}")
            println("Longitud del ID: ${matchAct.id.length}")
            
            val dto = matchAct.toDto()
            println("DTO creado - ID: '${dto.id}', MatchId: ${dto.matchId}")
            println("DTO - CompetitionId: ${dto.competitionId}, CompetitionName: ${dto.competitionName}")
            println("DTO - MainReferee: ${dto.mainReferee.name}")
            println("DTO - LocalTeam wrestlers: ${dto.localTeam.wrestlers.size}")
            println("DTO - VisitorTeam wrestlers: ${dto.visitorTeam.wrestlers.size}")
            
            // Verificar si es un ID temporal o un ObjectId de MongoDB
            val isNewAct = matchAct.id.startsWith("temp_") || matchAct.id.isEmpty()
            println("DECISIÓN: ¿Es acta nueva? $isNewAct")
            
            val savedDto = if (isNewAct) {
                // Es un acta nueva (ID temporal o vacío), usar POST
                println(">>> USANDO POST para crear nueva acta")
                matchActService.saveMatchAct(dto)
            } else {
                // Es una actualización (tiene un ID real de MongoDB), usar PUT
                println(">>> USANDO PUT para actualizar acta existente - ID: ${matchAct.id}")
                matchActService.updateMatchAct(matchAct.id, dto)
            }
            
            println("Respuesta recibida del servidor - ID: ${savedDto.id}")
            println("=== FIN saveMatchAct ===")
            savedDto.toDomain()
        } catch (e: Exception) {
            println("ERROR en HttpMatchActRepository.saveMatchAct: ${e.message}")
            e.printStackTrace()
            throw AppError.NetworkError("Error al guardar el acta: ${e.message}")
        }
    }
    
    override suspend fun completeMatchAct(actId: String): MatchAct {
        return try {
            val dto = matchActService.completeMatchAct(actId)
            dto.toDomain()
        } catch (e: Exception) {
            throw AppError.NetworkError("Error al completar el acta: ${e.message}")
        }
    }
    
    override suspend fun getAvailableReferees(): List<Referee> {
        return try {
            val dtos = matchActService.getAvailableReferees()
            dtos.map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

// Extension functions para mapear entre DTOs y Domain models

private fun MatchActDto.toDomain(): MatchAct {
    return MatchAct(
        id = this.id,
        matchId = this.matchId,
        competitionId = this.competitionId,
        competitionName = this.competitionName,
        season = this.season,
        category = AgeCategory.valueOf(this.category),
        isRegional = this.isRegional,
        isInsular = this.isInsular,
        venue = this.venue,
        date = LocalDate.parse(this.date),
        startTime = LocalTime.parse(this.startTime),
        endTime = this.endTime?.let { LocalTime.parse(it) },
        mainReferee = this.mainReferee.toDomain(),
        assistantReferees = this.assistantReferees.map { it.toDomain() },
        fieldDelegate = this.fieldDelegate?.toDomain(),
        localTeam = this.localTeam.toDomain(),
        visitorTeam = this.visitorTeam.toDomain(),
        bouts = this.bouts.map { it.toDomain() },
        localTeamScore = this.localTeamScore,
        visitorTeamScore = this.visitorTeamScore,
        isDraft = this.isDraft,
        isCompleted = this.isCompleted,
        isSigned = this.isSigned,
        localTeamComments = this.localTeamComments,
        visitorTeamComments = this.visitorTeamComments,
        refereeComments = this.refereeComments
    )
}

private fun MatchAct.toDto(): MatchActDto {
    // Si el ID es temporal, enviarlo vacío para que el servidor genere uno nuevo
    val dtoId = if (this.id.startsWith("temp_")) "" else this.id
    
    return MatchActDto(
        id = dtoId,
        matchId = this.matchId,
        competitionId = this.competitionId,
        competitionName = this.competitionName,
        season = this.season,
        category = this.category.name,
        isRegional = this.isRegional,
        isInsular = this.isInsular,
        venue = this.venue,
        date = this.date.toString(),
        startTime = this.startTime.toString(),
        endTime = this.endTime?.toString(),
        mainReferee = this.mainReferee.toDto(),
        assistantReferees = this.assistantReferees.map { it.toDto() },
        fieldDelegate = this.fieldDelegate?.toDto(),
        localTeam = this.localTeam.toDto(),
        visitorTeam = this.visitorTeam.toDto(),
        bouts = this.bouts.map { it.toDto() },
        localTeamScore = this.localTeamScore,
        visitorTeamScore = this.visitorTeamScore,
        isDraft = this.isDraft,
        isCompleted = this.isCompleted,
        isSigned = this.isSigned,
        localTeamComments = this.localTeamComments,
        visitorTeamComments = this.visitorTeamComments,
        refereeComments = this.refereeComments
    )
}

private fun RefereeDto.toDomain(): Referee {
    return Referee(
        id = this.id,
        name = this.name,
        licenseNumber = this.licenseNumber,
        isMain = this.isMain
    )
}

private fun Referee.toDto(): RefereeDto {
    return RefereeDto(
        id = this.id,
        name = this.name,
        licenseNumber = this.licenseNumber,
        isMain = this.isMain
    )
}

private fun FieldDelegateDto?.toDomain(): FieldDelegate? {
    return this?.let {
        FieldDelegate(
            name = it.name,
            dni = it.dni
        )
    }
}

private fun FieldDelegate?.toDto(): FieldDelegateDto? {
    return this?.let {
        FieldDelegateDto(
            name = it.name,
            dni = it.dni
        )
    }
}

private fun ActTeamDto.toDomain(): ActTeam {
    return ActTeam(
        teamId = this.teamId,
        clubName = this.clubName,
        wrestlers = this.wrestlers.map { it.toDomain() },
        captain = this.captain,
        coach = this.coach
    )
}

private fun ActTeam.toDto(): ActTeamDto {
    println("ActTeam.toDto - teamId: '${this.teamId}', clubName: '${this.clubName}'")
    println("ActTeam.toDto - wrestlers count: ${this.wrestlers.size}")
    println("ActTeam.toDto - captain: '${this.captain}', coach: '${this.coach}'")
    
    if (this.teamId.isBlank()) {
        println("WARNING: teamId está vacío!")
    }
    if (this.clubName.isBlank()) {
        println("WARNING: clubName está vacío!")
    }
    
    return ActTeamDto(
        teamId = this.teamId,
        clubName = this.clubName,
        wrestlers = this.wrestlers.map { it.toDto() },
        captain = this.captain,
        coach = this.coach
    )
}

private fun ActWrestlerDto.toDomain(): ActWrestler {
    // Map from DTO which has more fields to domain model which has fewer
    return ActWrestler(
        wrestlerId = this.wrestlerId,
        licenseNumber = this.licenseNumber,
        number = this.order
    )
}

private fun ActWrestler.toDto(): ActWrestlerDto {
    // Map from domain model to DTO, providing defaults for missing fields
    return ActWrestlerDto(
        wrestlerId = this.wrestlerId,
        order = this.number ?: 0,
        name = "", // Will be filled by the API
        licenseNumber = this.licenseNumber,
        classification = "NOVATO", // Default classification
        weight = 0.0 // Will be filled by the API
    )
}

private fun BoutDto.toDomain(): MatchBout {
    // Convert score format "X-Y" to falls
    val (localFalls, visitorFalls) = parseScoreToFalls(this.score ?: "0-0")
    
    // Determine winner based on result string
    val winner = when(this.result) {
        "LOCAL" -> WinnerType.LOCAL
        "VISITOR" -> WinnerType.VISITOR
        "DRAW" -> WinnerType.DRAW
        else -> WinnerType.NONE
    }
    
    return MatchBout(
        id = "bout_${this.boutNumber}",
        localWrestler = if (this.localWrestlerId.isNotEmpty()) {
            ActWrestler(
                wrestlerId = this.localWrestlerId,
                licenseNumber = "", // Not provided by DTO
                number = null
            )
        } else null,
        visitorWrestler = if (this.visitorWrestlerId.isNotEmpty()) {
            ActWrestler(
                wrestlerId = this.visitorWrestlerId,
                licenseNumber = "", // Not provided by DTO
                number = null
            )
        } else null,
        localFalls = localFalls,
        visitorFalls = visitorFalls,
        localPenalties = 0, // Not provided by DTO
        visitorPenalties = 0, // Not provided by DTO
        winner = winner,
        order = this.boutNumber
    )
}

private fun MatchBout.toDto(): BoutDto {
    // Determine the result string
    val result = when(this.winner) {
        WinnerType.LOCAL -> "LOCAL"
        WinnerType.VISITOR -> "VISITOR"
        WinnerType.DRAW -> "DRAW"
        WinnerType.NONE -> "NONE"
    }
    
    // Format score as "localFalls-visitorFalls"
    val score = "${this.localFalls.size}-${this.visitorFalls.size}"
    
    // Calculate points based on winner and falls
    val (localPoints, visitorPoints) = when(this.winner) {
        WinnerType.LOCAL -> {
            val points = if (this.localFalls.size >= 2) 2 else 1
            points to 0
        }
        WinnerType.VISITOR -> {
            val points = if (this.visitorFalls.size >= 2) 2 else 1
            0 to points
        }
        WinnerType.DRAW -> 0 to 0
        WinnerType.NONE -> 0 to 0
    }
    
    return BoutDto(
        boutNumber = this.order,
        localWrestlerId = this.localWrestler?.wrestlerId ?: "",
        visitorWrestlerId = this.visitorWrestler?.wrestlerId ?: "",
        localWrestlerName = "", // Will be filled by the API
        visitorWrestlerName = "", // Will be filled by the API
        result = result,
        score = score,
        time = "", // Not tracked in domain model
        localPoints = localPoints,
        visitorPoints = visitorPoints
    )
}

// Helper function to parse score string to falls
private fun parseScoreToFalls(score: String): Pair<List<Fall>, List<Fall>> {
    val parts = score.split("-")
    val localCount = parts.getOrNull(0)?.toIntOrNull() ?: 0
    val visitorCount = parts.getOrNull(1)?.toIntOrNull() ?: 0
    
    val localFalls = (1..localCount).map { Fall("local_fall_$it", FallType.REGULAR) }
    val visitorFalls = (1..visitorCount).map { Fall("visitor_fall_$it", FallType.REGULAR) }
    
    return localFalls to visitorFalls
}