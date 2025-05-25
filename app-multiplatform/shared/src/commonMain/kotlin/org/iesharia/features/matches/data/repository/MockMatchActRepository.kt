package org.iesharia.features.matches.data.repository

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.iesharia.core.data.mock.MockDataGenerator
import org.iesharia.features.competitions.domain.model.AgeCategory
import org.iesharia.features.matches.domain.model.*
import org.iesharia.features.matches.domain.repository.MatchActRepository
import org.iesharia.features.teams.domain.model.Match

/**
 * Implementación mock del repositorio de actas de enfrentamiento
 */
class MockMatchActRepository : MatchActRepository {
    
    // Almacenamiento en memoria para actas
    private val matchActs = mutableMapOf<String, MatchAct>()
    
    // Lista de árbitros mock
    private val referees = listOf(
        Referee("ref1", "Antonio Hernández", "REF-2023-001", isMain = true),
        Referee("ref2", "María Rodríguez", "REF-2023-002"),
        Referee("ref3", "Juan González", "REF-2023-003"),
        Referee("ref4", "Carmen Pérez", "REF-2023-004"),
        Referee("ref5", "Francisco Díaz", "REF-2023-005")
    )
    
    init {
        // Inicializar algunos datos mock
        val dataGenerator = MockDataGenerator()
        dataGenerator.generateAllData()
        
        val matches = dataGenerator.competitions.flatMap { it.matchDays }.flatMap { it.matches }
        matches.take(3).forEachIndexed { index, match ->
            val actId = "act_${index + 1}"
            val matchAct = MatchAct(
                id = actId,
                matchId = match.id,
                competitionId = "comp_${index + 1}",
                competitionName = "Liga Insular ${2023 + index}",
                season = "${2023 + index}",
                category = AgeCategory.REGIONAL,
                isRegional = index % 2 == 0,
                isInsular = index % 2 != 0,
                venue = match.venue,
                date = match.date.date,
                startTime = LocalTime(18, 0),
                endTime = LocalTime(20, 0),
                mainReferee = referees[0],
                assistantReferees = referees.subList(1, 3),
                fieldDelegate = FieldDelegate(
                    name = "José Martín",
                    dni = "12345678X"
                ),
                localTeam = ActTeam(
                    teamId = match.localTeam.id,
                    clubName = match.localTeam.name,
                    wrestlers = emptyList(),
                    captain = "Capitán Local",
                    coach = "Entrenador Local"
                ),
                visitorTeam = ActTeam(
                    teamId = match.visitorTeam.id,
                    clubName = match.visitorTeam.name,
                    wrestlers = emptyList(),
                    captain = "Capitán Visitante",
                    coach = "Entrenador Visitante"
                ),
                bouts = emptyList(),
                localTeamScore = match.localScore ?: 0,
                visitorTeamScore = match.visitorScore ?: 0,
                isDraft = true,
                isCompleted = false,
                isSigned = false
            )
            matchActs[actId] = matchAct
        }
    }

    override suspend fun getMatchAct(actId: String): MatchAct? {
        return matchActs[actId]
    }

    override suspend fun getMatchActByMatchId(matchId: String): MatchAct? {
        return matchActs.values.find { it.matchId == matchId }
    }

    override suspend fun saveMatchAct(matchAct: MatchAct): MatchAct {
        matchActs[matchAct.id] = matchAct
        return matchAct
    }

    override suspend fun completeMatchAct(actId: String): MatchAct {
        return matchActs[actId]?.let {
            val updated = it.copy(
                isDraft = false,
                isCompleted = true,
                isSigned = true
            )
            matchActs[actId] = updated
            updated
        } ?: throw IllegalArgumentException("No se encontró el acta con ID $actId")
    }

    override suspend fun getAvailableReferees(): List<Referee> {
        return referees
    }
}