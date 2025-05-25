package org.iesharia.features.matches.domain.model

/**
 * Datos de un árbitro auxiliar en el acta
 */
data class AssistantReferee(
    val name: String = "",
    val licenseNumber: String = ""
)

/**
 * Datos de un luchador en el acta
 */
data class MatchActWrestler(
    val id: String = "",
    val licenseNumber: String = ""
)

/**
 * Datos de una lucha en el acta
 */
data class MatchActBout(
    // Datos del luchador local
    val localWrestlerNumber: String = "",
    val localCheck1: Boolean = false,
    val localCheck2: Boolean = false,
    val localCheck3: Boolean = false, // NUEVA: tercera agarrada
    val localPenalty: String = "",

    // Datos del luchador visitante
    val visitorWrestlerNumber: String = "",
    val visitorCheck1: Boolean = false,
    val visitorCheck2: Boolean = false,
    val visitorCheck3: Boolean = false, // NUEVA: tercera agarrada
    val visitorPenalty: String = "",

    // Resultado
    val localScore: String = "0",
    val visitorScore: String = "0",
    val localWin: Boolean = false,
    val visitorWin: Boolean = false
)