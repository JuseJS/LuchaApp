package org.iesharia.features.matches.ui.viewmodel

import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.features.matches.domain.repository.MatchRepository
import org.iesharia.features.matches.domain.usecase.GetMatchDetailsUseCase

/**
 * ViewModel para la pantalla de acta de enfrentamiento
 */
class MatchActViewModel(
    private val matchId: String,
    private val getMatchDetailsUseCase: GetMatchDetailsUseCase,
    private val matchRepository: MatchRepository,
    navigationManager: NavigationManager,
    errorHandler: ErrorHandler
) : BaseViewModel<MatchActUiState>(MatchActUiState(), errorHandler, navigationManager) {

    init {
        loadMatchDetails()
    }

    private fun loadMatchDetails() {
        loadEntity(
            entityId = matchId,
            fetchEntity = {
                // Obtener detalles del enfrentamiento
                val (match, localTeamWrestlers, visitorTeamWrestlers) = getMatchDetailsUseCase(matchId)
                match
            },
            processEntity = { match ->
                // Obtener jornada
                val matchDay = matchRepository.getMatchDay(matchId)

                // Inicializar estado con datos del partido
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        match = match,
                        matchDay = matchDay,
                        season = match.date.year.toString(),
                        venue = match.venue,
                        competitionName = matchDay?.competitionId ?: "",
                        day = match.date.dayOfMonth.toString(),
                        month = match.date.monthNumber.toString(),
                        year = match.date.year.toString(),
                        localClubName = match.localTeam.name,
                        visitorClubName = match.visitorTeam.name
                    )
                }

                // Estado actual con datos parcialmente inicializados
                uiState.value
            }
        )
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
     * Actualiza el número de licencia de un luchador
     */
    fun updateWrestlerLicense(index: Int, licenseNumber: String, isLocal: Boolean) {
        updateState {
            if (isLocal) {
                val newWrestlers = it.localWrestlers.toMutableList()
                if (index < newWrestlers.size) {
                    newWrestlers[index] = newWrestlers[index].copy(licenseNumber = licenseNumber)
                }
                it.copy(localWrestlers = newWrestlers)
            } else {
                val newWrestlers = it.visitorWrestlers.toMutableList()
                if (index < newWrestlers.size) {
                    newWrestlers[index] = newWrestlers[index].copy(licenseNumber = licenseNumber)
                }
                it.copy(visitorWrestlers = newWrestlers)
            }
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
     * Guarda el acta (provisional)
     */
    fun saveAct() {
        // Implementación simulada - en producción, esto guardará los datos en el repositorio
        launchSafe {
            // Simulación de guardado
            updateState { it.copy(isSaved = true) }
        }
    }

    /**
     * Finaliza el acta y marca la jornada como finalizada
     */
    fun finishAct() {
        launchSafe {
            // Implementación simulada - en producción, esto finalizaría el acta y actualizaría la jornada
            // Navegar de vuelta a la pantalla anterior
            navigateBack()
        }
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