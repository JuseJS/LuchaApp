package org.iesharia.domain.services

import kotlinx.datetime.*
import org.bson.types.ObjectId
import org.iesharia.data.models.*
import org.iesharia.data.repositories.MatchActRepository
import org.iesharia.data.repositories.MatchRepository
import org.iesharia.data.repositories.RefereeRepository
import org.iesharia.domain.models.dto.*
import org.iesharia.domain.models.AgeCategory
import org.iesharia.domain.models.WrestlerClassification

class MatchActService(
    private val matchActRepository: MatchActRepository,
    private val matchRepository: MatchRepository,
    private val refereeRepository: RefereeRepository
) {
    suspend fun getMatchActById(id: String): MatchActDto? {
        val act = matchActRepository.findById(id) ?: return null
        return act.toDto()
    }
    
    suspend fun getMatchActByMatchId(matchId: String): MatchActDto? {
        return try {
            val act = matchActRepository.findByMatchId(matchId) ?: return null
            act.toDto()
        } catch (e: Exception) {
            println("MatchActService: Error al convertir acta a DTO para matchId $matchId - ${e.message}")
            e.printStackTrace()
            null
        }
    }
    
    suspend fun getAvailableReferees(): List<RefereeDto> {
        return refereeRepository.findActive().map { referee ->
            RefereeDto(
                id = referee.id,
                name = referee.name,
                licenseNumber = referee.licenseNumber,
                isMain = referee.isMain
            )
        }
    }
    
    suspend fun createMatchAct(request: MatchActDto): MatchActDto {
        println("MatchActService.createMatchAct - Iniciando...")
        println("MatchActService - request.matchId: ${request.matchId}")
        println("MatchActService - request.localTeam.teamId: '${request.localTeam.teamId}'")
        println("MatchActService - request.localTeam.clubName: '${request.localTeam.clubName}'")
        
        // Primero verificar si ya existe un acta para este match
        val existingAct = matchActRepository.findByMatchId(request.matchId)
        
        if (existingAct != null) {
            // Si ya existe, actualizar en lugar de crear
            println("MatchActService: Ya existe un acta para el match ${request.matchId}, actualizando...")
            return updateMatchAct(existingAct.id, request) 
                ?: throw Exception("Error al actualizar el acta existente")
        }
        
        // Si no existe, crear una nueva
        val now = Clock.System.now()
        
        println("MatchActService: Convirtiendo teams...")
        // Convert teams first to have wrestler info available
        val localTeamInfo = request.localTeam.toActTeamInfo()
        val visitorTeamInfo = request.visitorTeam.toActTeamInfo()
        
        println("MatchActService: localTeamInfo creado - teamId: '${localTeamInfo.teamId}', clubName: '${localTeamInfo.clubName}'")
        
        // Create wrestler lookup maps by ID
        val localWrestlerMap = localTeamInfo.wrestlers.associateBy { it.wrestlerId }
        val visitorWrestlerMap = visitorTeamInfo.wrestlers.associateBy { it.wrestlerId }
        
        // Convert bouts with proper wrestler info
        val boutsInfo = request.bouts.map { boutDto ->
            toMatchBoutInfoWithWrestlers(boutDto, localWrestlerMap, visitorWrestlerMap)
        }
        
        val document = MatchActDocument(
            _id = ObjectId(),
            matchId = request.matchId,
            competitionId = request.competitionId,
            competitionName = request.competitionName,
            season = request.season,
            category = AgeCategory.valueOf(request.category),
            isRegional = request.isRegional,
            isInsular = request.isInsular,
            venue = request.venue,
            date = LocalDate.parse(request.date),
            startTime = LocalTime.parse(request.startTime),
            endTime = request.endTime?.let { LocalTime.parse(it) },
            mainReferee = request.mainReferee.toRefereeInfo(),
            assistantReferees = request.assistantReferees.map { it.toRefereeInfo() },
            fieldDelegate = request.fieldDelegate?.toFieldDelegateInfo(),
            localTeam = localTeamInfo,
            visitorTeam = visitorTeamInfo,
            bouts = boutsInfo,
            localTeamScore = request.localTeamScore,
            visitorTeamScore = request.visitorTeamScore,
            isDraft = request.isDraft,
            isCompleted = request.isCompleted,
            isSigned = request.isSigned,
            localTeamComments = request.localTeamComments,
            visitorTeamComments = request.visitorTeamComments,
            refereeComments = request.refereeComments,
            createdAt = now,
            updatedAt = now
        )
        
        val saved = matchActRepository.create(document)
        
        // Update the match to indicate it has an act
        matchRepository.updateHasAct(request.matchId, true)
        
        return saved.toDto()
    }
    
    suspend fun updateMatchAct(id: String, request: MatchActDto): MatchActDto? {
        val existing = matchActRepository.findById(id) ?: return null
        val now = Clock.System.now()
        
        // Convert teams first to have wrestler info available
        val localTeamInfo = request.localTeam.toActTeamInfo()
        val visitorTeamInfo = request.visitorTeam.toActTeamInfo()
        
        // Create wrestler lookup maps by ID
        val localWrestlerMap = localTeamInfo.wrestlers.associateBy { it.wrestlerId }
        val visitorWrestlerMap = visitorTeamInfo.wrestlers.associateBy { it.wrestlerId }
        
        // Convert bouts with proper wrestler info
        val boutsInfo = request.bouts.map { boutDto ->
            toMatchBoutInfoWithWrestlers(boutDto, localWrestlerMap, visitorWrestlerMap)
        }
        
        val updated = existing.copy(
            endTime = request.endTime?.let { LocalTime.parse(it) } ?: existing.endTime,
            mainReferee = request.mainReferee.toRefereeInfo(),
            assistantReferees = request.assistantReferees.map { it.toRefereeInfo() },
            fieldDelegate = request.fieldDelegate?.toFieldDelegateInfo(),
            localTeam = localTeamInfo,
            visitorTeam = visitorTeamInfo,
            bouts = boutsInfo,
            localTeamScore = request.localTeamScore,
            visitorTeamScore = request.visitorTeamScore,
            localTeamComments = request.localTeamComments,
            visitorTeamComments = request.visitorTeamComments,
            refereeComments = request.refereeComments,
            updatedAt = now
        )
        
        val saved = matchActRepository.update(id, updated) ?: return null
        return saved.toDto()
    }
    
    suspend fun completeMatchAct(id: String): MatchActDto? {
        val act = matchActRepository.findById(id) ?: return null
        val now = Clock.System.now()
        
        val completed = act.copy(
            isDraft = false,
            isCompleted = true,
            isSigned = true,
            updatedAt = now
        )
        
        val saved = matchActRepository.update(id, completed) ?: return null
        
        // Update the match with the final score
        matchRepository.updateScore(
            act.matchId, 
            act.localTeamScore, 
            act.visitorTeamScore,
            completed = true
        )
        
        return saved.toDto()
    }
}

// Extension functions to convert between DTOs and Documents

private fun MatchActDocument.toDto(): MatchActDto {
    return MatchActDto(
        id = this.id,
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
        fieldDelegate = this.fieldDelegate?.toDto() ?: FieldDelegateDto("", ""),
        localTeam = this.localTeam.toDto(),
        visitorTeam = this.visitorTeam.toDto(),
        bouts = this.bouts.map { it.toDto() },
        localTeamScore = this.localTeamScore,
        visitorTeamScore = this.visitorTeamScore,
        isDraft = this.isDraft,
        isCompleted = this.isCompleted,
        isSigned = this.isSigned,
        observations = null,
        protests = null,
        localTeamComments = this.localTeamComments,
        visitorTeamComments = this.visitorTeamComments,
        refereeComments = this.refereeComments,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )
}

private fun RefereeInfo.toDto(): RefereeDto {
    return RefereeDto(
        id = this.id,
        name = this.name,
        licenseNumber = this.licenseNumber,
        isMain = this.isMain
    )
}

private fun RefereeDto.toRefereeInfo(): RefereeInfo {
    return RefereeInfo(
        id = this.id,
        name = this.name,
        licenseNumber = this.licenseNumber,
        isMain = this.isMain,
        isActive = true
    )
}

private fun FieldDelegateInfo.toDto(): FieldDelegateDto {
    return FieldDelegateDto(
        name = this.name,
        dni = this.dni
    )
}

private fun FieldDelegateDto?.toFieldDelegateInfo(): FieldDelegateInfo? {
    return this?.let {
        FieldDelegateInfo(
            name = it.name,
            dni = it.dni
        )
    }
}

private fun ActTeamInfo.toDto(): ActTeamDto {
    return ActTeamDto(
        teamId = this.teamId,
        clubName = this.clubName,
        wrestlers = this.wrestlers.map { it.toDto() },
        captain = this.captain,
        coach = this.coach
    )
}

private fun ActTeamDto.toActTeamInfo(): ActTeamInfo {
    // Validar campos requeridos
    require(this.teamId.isNotBlank()) { "teamId no puede estar vacío" }
    require(this.clubName.isNotBlank()) { "clubName no puede estar vacío" }
    
    return ActTeamInfo(
        teamId = this.teamId,
        clubName = this.clubName,
        wrestlers = this.wrestlers.map { it.toActWrestlerInfo() },
        captain = this.captain,
        coach = this.coach
    )
}

private fun ActWrestlerInfo.toDto(): ActWrestlerDto {
    return ActWrestlerDto(
        wrestlerId = this.wrestlerId,
        order = this.number ?: 0,
        name = "", // Name not stored in ActWrestlerInfo
        licenseNumber = this.licenseNumber,
        classification = WrestlerClassification.NONE.name, // Classification not stored in ActWrestlerInfo
        weight = null // Weight not stored in ActWrestlerInfo
    )
}

private fun ActWrestlerDto.toActWrestlerInfo(): ActWrestlerInfo {
    return ActWrestlerInfo(
        wrestlerId = this.wrestlerId,
        licenseNumber = this.licenseNumber,
        number = this.order
    )
}

private fun MatchBoutInfo.toDto(): BoutDto {
    // Calculate points based on winner and falls
    val localPoints = when (this.winner) {
        "LOCAL" -> if (this.localFalls.size >= 2) 2 else 1
        "DRAW" -> 0
        else -> 0
    }
    val visitorPoints = when (this.winner) {
        "VISITOR" -> if (this.visitorFalls.size >= 2) 2 else 1
        "DRAW" -> 0
        else -> 0
    }
    
    return BoutDto(
        boutNumber = this.order,
        localWrestlerId = this.localWrestler?.wrestlerId ?: "",
        visitorWrestlerId = this.visitorWrestler?.wrestlerId ?: "",
        localWrestlerName = "", // Name not stored in MatchBoutInfo
        visitorWrestlerName = "", // Name not stored in MatchBoutInfo
        result = this.winner,
        score = "${this.localFalls.size}-${this.visitorFalls.size}",
        time = null, // Time not stored in MatchBoutInfo
        localPoints = localPoints,
        visitorPoints = visitorPoints
    )
}

private fun toMatchBoutInfoWithWrestlers(
    boutDto: BoutDto,
    localWrestlerMap: Map<String, ActWrestlerInfo>,
    visitorWrestlerMap: Map<String, ActWrestlerInfo>
): MatchBoutInfo {
    // Generate a unique ID for the bout
    val boutId = ObjectId().toHexString()
    
    // Get wrestler info from maps
    val localWrestler = if (boutDto.localWrestlerId.isNotEmpty()) {
        localWrestlerMap[boutDto.localWrestlerId]
    } else null
    
    val visitorWrestler = if (boutDto.visitorWrestlerId.isNotEmpty()) {
        visitorWrestlerMap[boutDto.visitorWrestlerId]
    } else null
    
    // Parse falls from score if available
    val (localFallCount, visitorFallCount) = parseScore(boutDto.score)
    
    val localFalls = (1..localFallCount).map { i ->
        FallInfo(
            id = "fall_local_${boutDto.boutNumber}_$i",
            type = "REGULAR",
            inSeparation = false
        )
    }
    
    val visitorFalls = (1..visitorFallCount).map { i ->
        FallInfo(
            id = "fall_visitor_${boutDto.boutNumber}_$i",
            type = "REGULAR",
            inSeparation = false
        )
    }
    
    return MatchBoutInfo(
        id = boutId,
        localWrestler = localWrestler,
        visitorWrestler = visitorWrestler,
        localFalls = localFalls,
        visitorFalls = visitorFalls,
        localPenalties = 0, // Not provided in BoutDto
        visitorPenalties = 0, // Not provided in BoutDto
        winner = boutDto.result,
        order = boutDto.boutNumber
    )
}

private fun parseScore(score: String?): Pair<Int, Int> {
    if (score == null) return 0 to 0
    val parts = score.split("-")
    val localCount = parts.getOrNull(0)?.toIntOrNull() ?: 0
    val visitorCount = parts.getOrNull(1)?.toIntOrNull() ?: 0
    return localCount to visitorCount
}

private fun BoutDto.toMatchBoutInfo(): MatchBoutInfo {
    // Generate a unique ID for the bout
    val boutId = ObjectId().toHexString()
    
    // Create wrestler infos if IDs are provided
    val localWrestler = if (this.localWrestlerId.isNotEmpty()) {
        ActWrestlerInfo(
            wrestlerId = this.localWrestlerId,
            licenseNumber = "", // License number not provided in BoutDto
            number = null
        )
    } else null
    
    val visitorWrestler = if (this.visitorWrestlerId.isNotEmpty()) {
        ActWrestlerInfo(
            wrestlerId = this.visitorWrestlerId,
            licenseNumber = "", // License number not provided in BoutDto
            number = null
        )
    } else null
    
    // Parse falls from score if available
    val localFalls = mutableListOf<FallInfo>()
    val visitorFalls = mutableListOf<FallInfo>()
    
    // Determine winner based on points
    val winner = when {
        this.localPoints > this.visitorPoints -> "LOCAL"
        this.visitorPoints > this.localPoints -> "VISITOR"
        else -> "DRAW"
    }
    
    return MatchBoutInfo(
        id = boutId,
        localWrestler = localWrestler,
        visitorWrestler = visitorWrestler,
        localFalls = localFalls,
        visitorFalls = visitorFalls,
        localPenalties = 0,
        visitorPenalties = 0,
        winner = winner,
        order = this.boutNumber
    )
}