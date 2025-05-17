package org.iesharia.features.wrestlers.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.common.BadgeStatus
import org.iesharia.core.ui.components.common.InfoBadge
import org.iesharia.core.ui.components.common.ItemCard
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult

/**
 * Modelo para representar una agarrada en lucha canaria
 */
data class Grip(
    val ganador: String = "", // Nombre completo del ganador
    val esSeparada: Boolean = false // Indica si la agarrada fue separada
)

/**
 * Item para mostrar un luchador con sus resultados recientes
 */
@Composable
fun WrestlerItem(
    wrestler: Wrestler,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    matchResults: List<WrestlerMatchResult> = emptyList()
) {
    ItemCard(
        onClick = onClick,
        modifier = modifier,
        title = {
            // Cabecera del luchador
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar del luchador
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    val hasValidImage = wrestler.imageUrl != null && wrestler.imageUrl!!.isNotEmpty()
                    if (hasValidImage) {
                        // Implementar carga de imagen real
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(WrestlingTheme.dimensions.spacing_12))

                // Información básica del luchador
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = wrestler.fullName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = wrestler.position,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Logros/Clasificación del luchador - mostramos solo si hay logros adicionales
                        wrestler.achievements.firstOrNull()?.let {
                            InfoBadge(
                                text = it,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                backgroundColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }
        }
    ) {
        // Resultados de enfrentamientos
        if (matchResults.isNotEmpty()) {
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            Text(
                text = AppStrings.Wrestlers.recentMatches,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

            // Lista de resultados
            matchResults.forEach { result ->
                WrestlerMatchResultView(
                    result = result,
                    luchador = wrestler
                )
                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))
            }
        }
    }
}

/**
 * Componente para mostrar un encuentro de lucha canaria
 */
@Composable
fun WrestlerMatchResultView(
    result: WrestlerMatchResult,
    luchador: Wrestler
) {
    ItemCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentPadding = PaddingValues(WrestlingTheme.dimensions.spacing_12),
        title = {
            // Fecha y resultado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = result.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Badge de resultado usando InfoBadge con status
                val status = when(result.result) {
                    WrestlerMatchResult.Result.WIN -> BadgeStatus.SUCCESS
                    WrestlerMatchResult.Result.LOSS -> BadgeStatus.ERROR
                    WrestlerMatchResult.Result.DRAW -> BadgeStatus.WARNING
                    WrestlerMatchResult.Result.EXPELLED,
                    WrestlerMatchResult.Result.SEPARATED -> BadgeStatus.NEUTRAL
                }

                InfoBadge(
                    text = result.result.displayName(),
                    status = status
                )
            }
        }
    ) {
        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_12))

        // Enfrentamiento entre luchadores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Luchador principal
            WrestlerInfoColumn(
                nombre = luchador.fullName,
                clasificacion = luchador.position,
                faltas = result.fouls,
                modifier = Modifier.weight(1f)
            )

            // Separador central
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = AppStrings.Wrestlers.vs,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Categoría (Regional/Juvenil)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = result.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            // Oponente
            WrestlerInfoColumn(
                nombre = result.opponentName,
                clasificacion = result.classification,
                faltas = result.opponentFouls,
                modifier = Modifier.weight(1f)
            )
        }

        // Agarradas
        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_12))

        // Obtenemos las agarradas del resultado
        val agarradas = getGripsFromResult(result, luchador.fullName)

        Text(
            text = AppStrings.Wrestlers.agarradas,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mostrar agarradas
        agarradas.forEachIndexed { index, agarrada ->
            AgarradaItem(
                grip = agarrada,
                numeroAgarrada = index + 1
            )

            if (index < agarradas.size - 1) {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

/**
 * Columna que muestra la información de un luchador
 */
@Composable
private fun WrestlerInfoColumn(
    nombre: String,
    clasificacion: String,
    faltas: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Nombre
        Text(
            text = nombre,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Clasificación usando InfoBadge
        InfoBadge(
            text = clasificacion,
            color = MaterialTheme.colorScheme.secondary,
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Faltas usando InfoBadge con colores condicionales
        InfoBadge(
            text = AppStrings.Wrestlers.foulsFormat.format(faltas),
            color = if (faltas > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
            backgroundColor = if (faltas > 0)
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

/**
 * Componente para mostrar una agarrada individual
 */
@Composable
private fun AgarradaItem(
    grip: Grip,
    numeroAgarrada: Int
) {
    Surface(
        shape = WrestlingTheme.shapes.small,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Número de agarrada
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(24.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "$numeroAgarrada",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Información de la agarrada
            if (grip.esSeparada) {
                Text(
                    text = AppStrings.Wrestlers.separated,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Medium
                )
            } else if (grip.ganador.isNotEmpty()) {
                Text(
                    text = grip.ganador,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            } else {
                Text(
                    text = AppStrings.Wrestlers.noResult,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Obtiene las agarradas a partir de un resultado
 */
private fun getGripsFromResult(result: WrestlerMatchResult, luchadorNombre: String): List<Grip> {
    val grips = mutableListOf<Grip>()

    when (result.result) {
        WrestlerMatchResult.Result.WIN -> {
            // Victoria, el luchador gana 2-3 agarradas
            grips.add(Grip(ganador = luchadorNombre))
            grips.add(Grip(ganador = result.opponentName))
            grips.add(Grip(ganador = luchadorNombre))
        }
        WrestlerMatchResult.Result.LOSS -> {
            // Derrota, el oponente gana 2-3 agarradas
            grips.add(Grip(ganador = result.opponentName))
            grips.add(Grip(ganador = luchadorNombre))
            grips.add(Grip(ganador = result.opponentName))
        }
        WrestlerMatchResult.Result.DRAW -> {
            // Empate, normalmente 2-2
            grips.add(Grip(ganador = luchadorNombre))
            grips.add(Grip(ganador = result.opponentName))
            grips.add(Grip(ganador = luchadorNombre))
            grips.add(Grip(ganador = result.opponentName))
        }
        WrestlerMatchResult.Result.EXPELLED -> {
            // Expulsión, una agarrada y luego expulsión
            grips.add(Grip(ganador = result.opponentName))
            grips.add(Grip(esSeparada = true))
        }
        WrestlerMatchResult.Result.SEPARATED -> {
            // Separación, normalmente una agarrada y luego separación
            grips.add(Grip(ganador = luchadorNombre))
            grips.add(Grip(esSeparada = true))
        }
    }
    return grips
}