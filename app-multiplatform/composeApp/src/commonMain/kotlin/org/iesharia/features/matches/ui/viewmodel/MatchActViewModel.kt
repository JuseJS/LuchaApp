package org.iesharia.features.matches.ui.viewmodel

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.features.competitions.domain.model.AgeCategory
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.competitions.domain.usecase.GetCompetitionsUseCase
import org.iesharia.features.matches.domain.model.*
import org.iesharia.features.matches.domain.repository.MatchActRepository
import org.iesharia.features.matches.domain.repository.MatchRepository
import org.iesharia.features.matches.domain.usecase.GetMatchActUseCase
import org.iesharia.features.matches.domain.usecase.GetMatchDetailsUseCase
import org.iesharia.features.matches.domain.usecase.SaveMatchActUseCase
import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.wrestlers.domain.usecase.GetWrestlersByTeamIdUseCase

/**
 * ViewModel para la pantalla de acta de enfrentamiento
 */
class MatchActViewModel(
    private val matchId: String,
    private val getMatchDetailsUseCase: GetMatchDetailsUseCase,
    private val getMatchActUseCase: GetMatchActUseCase,
    private val saveMatchActUseCase: SaveMatchActUseCase,
    private val matchRepository: MatchRepository,
    private val matchActRepository: MatchActRepository,
    private val getCompetitionsUseCase: GetCompetitionsUseCase,
    private val getWrestlersByTeamIdUseCase: GetWrestlersByTeamIdUseCase,
    navigationManager: NavigationManager,
    errorHandler: ErrorHandler
) : BaseViewModel<MatchActUiState>(MatchActUiState(), errorHandler, navigationManager) {

    // Almacenar datos para usar en la UI
    private var availableCompetitions: List<Competition> = emptyList()
    private var availableReferees: List<Referee> = emptyList()
    private var localTeamWrestlers: List<Wrestler> = emptyList()
    private var visitorTeamWrestlers: List<Wrestler> = emptyList()

    init {
        // Modificado para cargar primero las competiciones antes de cargar los detalles del enfrentamiento
        loadCompetitions()
        loadMatchDetails()
        loadReferees()
    }

    private fun loadMatchDetails() {
        loadEntity(
            entityId = matchId,
            fetchEntity = {
                // Obtener detalles del enfrentamiento
                val (match, localWrestlers, visitorWrestlers) = getMatchDetailsUseCase(matchId)
                localTeamWrestlers = localWrestlers
                visitorTeamWrestlers = visitorWrestlers

                // Obtener acta existente o crear una nueva
                val existingAct = getMatchActUseCase.byMatchId(matchId)
                Triple(match, existingAct, matchRepository.getMatchDay(matchId))
            },
            processEntity = { (match, existingAct, matchDay) ->
                // Inicializar estado con datos del partido
                updateState {
                    val competitionId = matchDay?.competitionId ?: ""
                    // Obtener el nombre de la competición basado en su ID
                    val competitionName = getCompetitionName(competitionId)

                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        match = match,
                        matchDay = matchDay,
                        actId = existingAct?.id,
                        season = match.date.year.toString(),
                        venue = match.venue,
                        competitionId = competitionId,
                        competitionName = competitionName, // Ahora usamos el nombre real
                        day = match.date.dayOfMonth.toString(),
                        month = match.date.monthNumber.toString(),
                        year = match.date.year.toString(),
                        localClubName = match.localTeam.name,
                        visitorClubName = match.visitorTeam.name
                    )
                }

                if (existingAct != null) {
                    // Si existe un acta, cargar todos sus datos
                    updateFromExistingAct(existingAct)
                }

                // Estado actual con datos parcialmente inicializados
                uiState.value
            }
        )
    }

    /**
     * Obtiene el nombre de una competición a partir de su ID
     */
    private fun getCompetitionName(competitionId: String): String {
        return availableCompetitions.find { it.id == competitionId }?.name ?: competitionId
    }

    private fun updateFromExistingAct(act: MatchAct) {
        updateState {
            it.copy(
                isRegional = act.isRegional,
                isInsular = act.isInsular,
                category = act.category.displayName(),
                competitionId = act.competitionId,
                competitionName = act.competitionName,
                season = act.season,
                venue = act.venue,
                day = act.date.dayOfMonth.toString(),
                month = act.date.month.toString(),
                year = act.date.year.toString(),
                startTime = formatTime(act.startTime),
                endTime = act.endTime?.let { time -> formatTime(time) } ?: "",
                referee = act.mainReferee.name,
                refereeLicense = act.mainReferee.licenseNumber,
                assistantReferees = act.assistantReferees.map { ref ->
                    AssistantReferee(ref.name, ref.licenseNumber)
                },
                fieldDelegate = act.fieldDelegate?.name ?: "",
                fieldDelegateDni = act.fieldDelegate?.dni ?: "",
                localClubName = act.localTeam.clubName,
                visitorClubName = act.visitorTeam.clubName,
                localWrestlers = act.localTeam.wrestlers.map { wrestler ->
                    MatchActWrestler(wrestler.wrestlerId, wrestler.licenseNumber)
                },
                visitorWrestlers = act.visitorTeam.wrestlers.map { wrestler ->
                    MatchActWrestler(wrestler.wrestlerId, wrestler.licenseNumber)
                },
                localCaptain = act.localTeam.captain,
                visitorCaptain = act.visitorTeam.captain,
                localCoach = act.localTeam.coach,
                visitorCoach = act.visitorTeam.coach,
                // Mapear las luchas - ACTUALIZADO para incluir la tercera agarrada
                bouts = act.bouts.map { bout ->
                    MatchActBout(
                        localWrestlerNumber = bout.localWrestler?.number?.toString() ?: "",
                        localCheck1 = bout.localFalls.size > 0,
                        localCheck2 = bout.localFalls.size > 1,
                        localCheck3 = bout.localFalls.size > 2, // NUEVA: tercera agarrada
                        localPenalty = bout.localPenalties.toString(),
                        visitorWrestlerNumber = bout.visitorWrestler?.number?.toString() ?: "",
                        visitorCheck1 = bout.visitorFalls.size > 0,
                        visitorCheck2 = bout.visitorFalls.size > 1,
                        visitorCheck3 = bout.visitorFalls.size > 2, // NUEVA: tercera agarrada
                        visitorPenalty = bout.visitorPenalties.toString(),
                        localWin = bout.winner == WinnerType.LOCAL,
                        visitorWin = bout.winner == WinnerType.VISITOR
                    )
                },
                localTeamScore = act.localTeamScore.toString(),
                visitorTeamScore = act.visitorTeamScore.toString()
            )
        }
    }

    private fun formatTime(time: LocalTime): String {
        return "${time.hour.toString().padStart(2, '0')}:${time.minute.toString().padStart(2, '0')}"
    }

    private fun loadCompetitions() {
        launchSafe {
            availableCompetitions = getCompetitionsUseCase()
        }
    }

    private fun loadReferees() {
        launchSafe {
            availableReferees = matchActRepository.getAvailableReferees()
        }
    }

    // Métodos para obtener datos para la UI
    fun getAvailableCompetitions(): List<Competition> = availableCompetitions

    fun getAvailableReferees(): List<Referee> = availableReferees

    fun getAvailableWrestlers(isLocal: Boolean): List<Wrestler> {
        return if (isLocal) localTeamWrestlers else visitorTeamWrestlers
    }

    /**
     * Añade un nuevo luchador al equipo especificado
     */
    fun addWrestler(isLocal: Boolean) {
        updateState {
            if (isLocal) {
                val newWrestlers = it.localWrestlers.toMutableList()
                newWrestlers.add(MatchActWrestler())
                it.copy(localWrestlers = newWrestlers)
            } else {
                val newWrestlers = it.visitorWrestlers.toMutableList()
                newWrestlers.add(MatchActWrestler())
                it.copy(visitorWrestlers = newWrestlers)
            }
        }
    }

    /**
     * Elimina un luchador del equipo especificado
     */
    fun removeWrestler(index: Int, isLocal: Boolean) {
        updateState {
            if (isLocal) {
                val newWrestlers = it.localWrestlers.toMutableList()
                if (index < newWrestlers.size) {
                    newWrestlers.removeAt(index)
                }
                it.copy(localWrestlers = newWrestlers)
            } else {
                val newWrestlers = it.visitorWrestlers.toMutableList()
                if (index < newWrestlers.size) {
                    newWrestlers.removeAt(index)
                }
                it.copy(visitorWrestlers = newWrestlers)
            }
        }
    }

    /**
     * Actualiza los datos de un luchador
     */
    fun updateWrestler(index: Int, wrestlerId: String, licenseNumber: String, isLocal: Boolean) {
        updateState {
            if (isLocal) {
                val newWrestlers = it.localWrestlers.toMutableList()
                if (index < newWrestlers.size) {
                    newWrestlers[index] = MatchActWrestler(wrestlerId, licenseNumber)
                } else {
                    newWrestlers.add(MatchActWrestler(wrestlerId, licenseNumber))
                }
                it.copy(localWrestlers = newWrestlers)
            } else {
                val newWrestlers = it.visitorWrestlers.toMutableList()
                if (index < newWrestlers.size) {
                    newWrestlers[index] = MatchActWrestler(wrestlerId, licenseNumber)
                } else {
                    newWrestlers.add(MatchActWrestler(wrestlerId, licenseNumber))
                }
                it.copy(visitorWrestlers = newWrestlers)
            }
        }
    }

    /**
     * Añade un árbitro auxiliar al acta
     */
    fun addAssistantReferee(name: String, licenseNumber: String) {
        updateState {
            val newAssistants = it.assistantReferees.toMutableList()
            newAssistants.add(AssistantReferee(name, licenseNumber))
            it.copy(assistantReferees = newAssistants)
        }
    }

    /**
     * Elimina un árbitro auxiliar del acta
     */
    fun removeAssistantReferee(index: Int) {
        updateState {
            val newAssistants = it.assistantReferees.toMutableList()
            if (index < newAssistants.size) {
                newAssistants.removeAt(index)
            }
            it.copy(assistantReferees = newAssistants)
        }
    }

    /**
     * Añade una nueva lucha al acta
     */
    fun addBout() {
        updateState {
            val newBouts = it.bouts.toMutableList()
            newBouts.add(MatchActBout())
            it.copy(bouts = newBouts)
        }
    }

    /**
     * Elimina una lucha del acta
     */
    fun removeBout(index: Int) {
        updateState {
            val newBouts = it.bouts.toMutableList()
            if (index < newBouts.size) {
                newBouts.removeAt(index)
            }
            it.copy(bouts = newBouts)
        }
    }

    /**
     * Actualiza los datos de una lucha
     */
    fun updateBout(index: Int, bout: MatchActBout) {
        updateState {
            val newBouts = it.bouts.toMutableList()
            if (index < newBouts.size) {
                newBouts[index] = bout
            }
            it.copy(bouts = newBouts)
        }
    }

    /**
     * Guarda el acta
     */
    fun saveAct() {
        val state = uiState.value
        val match = state.match ?: return

        launchSafe {
            // Crear o actualizar el objeto MatchAct a partir del estado UI
            val act = createMatchActFromState(state)

            // Guardar acta
            val savedAct = saveMatchActUseCase(act)

            // Actualizar el estado
            updateState {
                it.copy(
                    isSaved = true,
                    actId = savedAct.id
                )
            }
        }
    }

    /**
     * Crea un objeto MatchAct a partir del estado UI actual
     */
    private fun createMatchActFromState(state: MatchActUiState): MatchAct {
        val match = state.match ?: throw IllegalStateException("No hay enfrentamiento asociado")

        // Construir luchadores del equipo local
        val localWrestlers = state.localWrestlers.mapIndexed { index, wrestler ->
            ActWrestler(
                wrestlerId = wrestler.id,
                licenseNumber = wrestler.licenseNumber,
                number = index + 1
            )
        }

        // Construir luchadores del equipo visitante
        val visitorWrestlers = state.visitorWrestlers.mapIndexed { index, wrestler ->
            ActWrestler(
                wrestlerId = wrestler.id,
                licenseNumber = wrestler.licenseNumber,
                number = index + 1
            )
        }

        // Construir luchas - ACTUALIZADO para incluir la tercera agarrada
        val bouts = state.bouts.mapIndexed { index, bout ->
            // Encontrar los luchadores por su número
            val localWrestlerNumber = bout.localWrestlerNumber.toIntOrNull()
            val visitorWrestlerNumber = bout.visitorWrestlerNumber.toIntOrNull()

            val localWrestler = localWrestlerNumber?.let { number ->
                localWrestlers.find { it.number == number }
            }

            val visitorWrestler = visitorWrestlerNumber?.let { number ->
                visitorWrestlers.find { it.number == number }
            }

            // Determinar el ganador
            val winner = when {
                bout.localWin -> WinnerType.LOCAL
                bout.visitorWin -> WinnerType.VISITOR
                else -> WinnerType.NONE
            }

            // Crear caídas según los checkboxes - ACTUALIZADO para incluir la tercera agarrada
            val localFalls = mutableListOf<Fall>()
            if (bout.localCheck1) {
                localFalls.add(Fall("fall_local_1_$index", FallType.REGULAR))
            }
            if (bout.localCheck2) {
                localFalls.add(Fall("fall_local_2_$index", FallType.REGULAR))
            }
            if (bout.localCheck3) { // NUEVA: tercera agarrada
                localFalls.add(Fall("fall_local_3_$index", FallType.REGULAR))
            }

            val visitorFalls = mutableListOf<Fall>()
            if (bout.visitorCheck1) {
                visitorFalls.add(Fall("fall_visitor_1_$index", FallType.REGULAR))
            }
            if (bout.visitorCheck2) {
                visitorFalls.add(Fall("fall_visitor_2_$index", FallType.REGULAR))
            }
            if (bout.visitorCheck3) { // NUEVA: tercera agarrada
                visitorFalls.add(Fall("fall_visitor_3_$index", FallType.REGULAR))
            }

            MatchBout(
                id = "bout_$index",
                localWrestler = localWrestler,
                visitorWrestler = visitorWrestler,
                localFalls = localFalls,
                visitorFalls = visitorFalls,
                localPenalties = bout.localPenalty.toIntOrNull() ?: 0,
                visitorPenalties = bout.visitorPenalty.toIntOrNull() ?: 0,
                winner = winner,
                order = index + 1
            )
        }

        // Crear el objeto de acta
        return MatchAct(
            id = state.actId ?: "act_${match.id}",
            matchId = match.id,
            competitionId = state.competitionId,
            competitionName = state.competitionName,
            season = state.season,
            category = if (state.category.lowercase().contains("regional"))
                AgeCategory.REGIONAL else AgeCategory.JUVENIL,
            isRegional = state.isRegional,
            isInsular = state.isInsular,
            venue = state.venue,
            date = parseDate(state.day, state.month, state.year),
            startTime = parseTime(state.startTime),
            endTime = if (state.endTime.isNotBlank()) parseTime(state.endTime) else null,
            mainReferee = Referee(
                id = "main_referee",
                name = state.referee,
                licenseNumber = state.refereeLicense,
                isMain = true
            ),
            assistantReferees = state.assistantReferees.map { assistant ->
                Referee(
                    id = "assistant_${assistant.licenseNumber}",
                    name = assistant.name,
                    licenseNumber = assistant.licenseNumber,
                    isMain = false
                )
            },
            fieldDelegate = if (state.fieldDelegate.isNotBlank()) {
                FieldDelegate(state.fieldDelegate, state.fieldDelegateDni)
            } else null,
            localTeam = ActTeam(
                teamId = match.localTeam.id,
                clubName = state.localClubName,
                wrestlers = localWrestlers,
                captain = state.localCaptain,
                coach = state.localCoach
            ),
            visitorTeam = ActTeam(
                teamId = match.visitorTeam.id,
                clubName = state.visitorClubName,
                wrestlers = visitorWrestlers,
                captain = state.visitorCaptain,
                coach = state.visitorCoach
            ),
            bouts = bouts,
            localTeamScore = state.localTeamScore.toIntOrNull() ?: 0,
            visitorTeamScore = state.visitorTeamScore.toIntOrNull() ?: 0,
            isDraft = true,
            isCompleted = false,
            isSigned = false
        )
    }

    /**
     * Finaliza el acta y marca la jornada como finalizada
     */
    fun finishAct() {
        val state = uiState.value

        launchSafe {
            // Primero guardar el acta
            saveAct()

            // Finalizar el acta si hay un ID
            state.actId?.let { actId ->
                val completedAct = saveMatchActUseCase.complete(actId)

                // Actualizar estado
                updateState {
                    it.copy(isSaved = true)
                }

                // Navegar de vuelta a la pantalla anterior
                navigateBack()
            } ?: run {
                // Si no hay ID, intentar guardar primero
                saveAct()
                navigateBack()
            }
        }
    }

    /**
     * Parsea una fecha a partir de componentes de texto
     */
    private fun parseDate(day: String, month: String, year: String): LocalDate {
        val d = day.toIntOrNull() ?: 1
        val m = when (month.lowercase()) {
            "enero", "1", "01" -> 1
            "febrero", "2", "02" -> 2
            "marzo", "3", "03" -> 3
            "abril", "4", "04" -> 4
            "mayo", "5", "05" -> 5
            "junio", "6", "06" -> 6
            "julio", "7", "07" -> 7
            "agosto", "8", "08" -> 8
            "septiembre", "9", "09" -> 9
            "octubre", "10" -> 10
            "noviembre", "11" -> 11
            "diciembre", "12" -> 12
            else -> month.toIntOrNull() ?: 1
        }
        val y = year.toIntOrNull() ?: 2023

        return LocalDate(y, m, d)
    }

    /**
     * Parsea una hora a partir de un texto
     */
    private fun parseTime(time: String): LocalTime {
        val parts = time.split(":")
        val hour = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0

        return LocalTime(hour, minute)
    }

    // Métodos de actualización para todos los campos del acta
    fun setIsInsular(value: Boolean) {
        updateState {
            it.copy(
                isInsular = value,
                isRegional = if (value) false else it.isRegional
            )
        }
    }

    fun setIsRegional(value: Boolean) {
        updateState {
            it.copy(
                isRegional = value,
                isInsular = if (value) false else it.isInsular
            )
        }
    }

    fun setSeason(value: String) {
        updateState { it.copy(season = value) }
    }

    fun setCategory(value: String) {
        updateState { it.copy(category = value) }
    }

    fun setCompetitionName(value: String) {
        updateState { it.copy(competitionName = value) }
    }

    fun setCompetitionId(value: String) {
        // Actualizar tanto el ID como el nombre de la competición
        updateState {
            val competitionName = getCompetitionName(value)
            it.copy(
                competitionId = value,
                competitionName = competitionName
            )
        }
    }

    fun setVenue(value: String) {
        updateState { it.copy(venue = value) }
    }

    fun setDay(value: String) {
        updateState { it.copy(day = value) }
    }

    fun setMonth(value: String) {
        updateState { it.copy(month = value) }
    }

    fun setYear(value: String) {
        updateState { it.copy(year = value) }
    }

    fun setStartTime(value: String) {
        updateState { it.copy(startTime = value) }
    }

    fun setEndTime(value: String) {
        updateState { it.copy(endTime = value) }
    }

    fun setReferee(value: String) {
        updateState { it.copy(referee = value) }
    }

    fun setRefereeLicense(value: String) {
        updateState { it.copy(refereeLicense = value) }
    }

    fun setFieldDelegate(value: String) {
        updateState { it.copy(fieldDelegate = value) }
    }

    fun setFieldDelegateDni(value: String) {
        updateState { it.copy(fieldDelegateDni = value) }
    }

    fun setLocalClubName(value: String) {
        updateState { it.copy(localClubName = value) }
    }

    fun setVisitorClubName(value: String) {
        updateState { it.copy(visitorClubName = value) }
    }

    fun setLocalCaptain(value: String) {
        updateState { it.copy(localCaptain = value) }
    }

    fun setVisitorCaptain(value: String) {
        updateState { it.copy(visitorCaptain = value) }
    }

    fun setLocalCoach(value: String) {
        updateState { it.copy(localCoach = value) }
    }

    fun setVisitorCoach(value: String) {
        updateState { it.copy(visitorCoach = value) }
    }

    fun setLocalTeamScore(value: String) {
        updateState { it.copy(localTeamScore = value) }
    }

    fun setVisitorTeamScore(value: String) {
        updateState { it.copy(visitorTeamScore = value) }
    }
}