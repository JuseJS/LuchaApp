package org.iesharia.features.wrestlers.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.common.BadgeStatus
import org.iesharia.core.ui.components.common.EmptyStateMessage
import org.iesharia.core.ui.components.common.InfoBadge
import org.iesharia.core.ui.components.common.SectionDivider
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult

/**
 * Sección de historial de enfrentamientos
 */
@Composable
fun WrestlerMatchHistorySection(
    matchResults: List<WrestlerMatchResult>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WrestlingTheme.dimensions.spacing_16)
    ) {
        // Reemplazar el Row con nuestro SectionDivider reutilizable
        SectionDivider(title = AppStrings.Wrestlers.recentMatches)

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

        if (matchResults.isEmpty()) {
            EmptyStateMessage(message = "No hay enfrentamientos registrados")
        } else {
            // Historial de enfrentamientos
            matchResults.forEach { result ->
                WrestlerMatchResultCard(result = result)
                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))
            }
        }
    }
}

/**
 * Tarjeta de resultado de enfrentamiento
 */
@Composable
fun WrestlerMatchResultCard(
    result: WrestlerMatchResult
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = WrestlingTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WrestlingTheme.dimensions.spacing_16)
        ) {
            // Cabecera con fecha y resultado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = result.date,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Badge de resultado
                val status = when(result.result) {
                    WrestlerMatchResult.Result.WIN -> BadgeStatus.SUCCESS
                    WrestlerMatchResult.Result.LOSS -> BadgeStatus.ERROR
                    WrestlerMatchResult.Result.DRAW -> BadgeStatus.WARNING
                    else -> BadgeStatus.NEUTRAL
                }

                InfoBadge(
                    text = result.result.displayName(),
                    status = status
                )
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Información del oponente
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar del oponente
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(WrestlingTheme.dimensions.spacing_16))

                // Datos del oponente
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = result.opponentName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_4))

                    // Clasificación
                    Text(
                        text = result.classification,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Información de faltas
                Column(horizontalAlignment = Alignment.End) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
                    ) {
                        // Faltas propias
                        InfoBadge(
                            text = "Faltas: ${result.fouls}",
                            color = if (result.fouls > 0)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant,
                            backgroundColor = if (result.fouls > 0)
                                MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f)
                        )

                        // Faltas del oponente
                        InfoBadge(
                            text = "Oponente: ${result.opponentFouls}",
                            color = if (result.opponentFouls > 0)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant,
                            backgroundColor = if (result.opponentFouls > 0)
                                MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f)
                        )
                    }
                }
            }

            // Agarradas (opcional - mostrar si hay espacio)
            if (result.result != WrestlerMatchResult.Result.SEPARATED &&
                result.result != WrestlerMatchResult.Result.EXPELLED) {

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_12))

                Text(
                    text = AppStrings.Wrestlers.agarradas,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

                // Lista de agarradas basada en el resultado
                val grips = createGripsFromResult(result, "Luchador")

                grips.forEachIndexed { index, grip ->
                    GripItem(
                        gripNumber = index + 1,
                        isWin = grip.first,
                        isSeparated = grip.second
                    )

                    if (index < grips.size - 1) {
                        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_4))
                    }
                }
            }
        }
    }
}

/**
 * Elemento individual de agarrada
 */
@Composable
private fun GripItem(
    gripNumber: Int,
    isWin: Boolean,
    isSeparated: Boolean
) {
    Surface(
        shape = WrestlingTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
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
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                modifier = Modifier.size(24.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "$gripNumber",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Información de la agarrada
            Text(
                text = when {
                    isSeparated -> AppStrings.Wrestlers.separated
                    isWin -> "Victoria"
                    else -> "Derrota"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = when {
                    isSeparated -> MaterialTheme.colorScheme.tertiary
                    isWin -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.error
                },
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Crea una representación de agarradas basada en el resultado
 * @return Lista de pares (isWin, isSeparated)
 */
private fun createGripsFromResult(
    result: WrestlerMatchResult,
    wrestlerName: String
): List<Pair<Boolean, Boolean>> {
    return when (result.result) {
        WrestlerMatchResult.Result.WIN -> listOf(
            Pair(true, false),
            Pair(false, false),
            Pair(true, false)
        )
        WrestlerMatchResult.Result.LOSS -> listOf(
            Pair(false, false),
            Pair(true, false),
            Pair(false, false)
        )
        WrestlerMatchResult.Result.DRAW -> listOf(
            Pair(true, false),
            Pair(false, false),
            Pair(true, false),
            Pair(false, false)
        )
        WrestlerMatchResult.Result.EXPELLED -> listOf(
            Pair(false, false),
            Pair(false, true)
        )
        WrestlerMatchResult.Result.SEPARATED -> listOf(
            Pair(true, false),
            Pair(false, true)
        )
    }
}