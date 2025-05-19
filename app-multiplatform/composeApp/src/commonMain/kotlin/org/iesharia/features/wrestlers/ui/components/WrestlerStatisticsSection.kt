package org.iesharia.features.wrestlers.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.components.common.SectionDivider
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.wrestlers.domain.model.WrestlerClassification
import org.iesharia.features.wrestlers.ui.viewmodel.WrestlerStatistics

/**
 * Sección de estadísticas de rendimiento del luchador
 */
@Composable
fun WrestlerStatisticsSection(
    statisticsByClassification: Map<WrestlerClassification, WrestlerStatistics>
) {
    // Calcular totales para el resumen
    val totalMatches = statisticsByClassification.values.sumOf { it.total }
    val totalWins = statisticsByClassification.values.sumOf { it.wins }
    val totalLosses = statisticsByClassification.values.sumOf { it.losses }
    val totalDraws = statisticsByClassification.values.sumOf { it.draws }
    val totalPoints = statisticsByClassification.values.sumOf { it.points }
    val overallEffectiveness = if (totalMatches > 0) {
        (totalPoints / totalMatches) * 100.0
    } else {
        0.0
    }

    // Categorizar por tipo de luchador
    val puntalesMatches = statisticsByClassification
        .filter { it.key in listOf(WrestlerClassification.PUNTAL_A, WrestlerClassification.PUNTAL_B, WrestlerClassification.PUNTAL_C) }
        .values.sumOf { it.total }

    val destacadosMatches = statisticsByClassification
        .filter { it.key in listOf(WrestlerClassification.DESTACADO_A, WrestlerClassification.DESTACADO_B, WrestlerClassification.DESTACADO_C) }
        .values.sumOf { it.total }

    val otherMatches = statisticsByClassification
        .filter { it.key == WrestlerClassification.NONE }
        .values.sumOf { it.total }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WrestlingTheme.dimensions.spacing_16)
    ) {
        // Reemplazado el título con el componente SectionDivider
        SectionDivider(title = "Rendimiento")

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

        // Resumen de temporada con mejor visualización
        SeasonSummaryCard(
            totalMatches = totalMatches,
            wins = totalWins,
            losses = totalLosses,
            draws = totalDraws,
            effectivenessPercentage = overallEffectiveness
        )

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

        // Tabla detallada de estadísticas por clasificación
        DetailedStatisticsTable(statisticsByClassification)

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

        // Enfrentamientos por tipo de luchador
        OpponentTypeBreakdown(
            puntalesMatches = puntalesMatches,
            destacadosMatches = destacadosMatches,
            otherMatches = otherMatches
        )
    }
}

/**
 * Tarjeta de resumen de temporada
 */
@Composable
fun SeasonSummaryCard(
    totalMatches: Int,
    wins: Int,
    losses: Int,
    draws: Int,
    effectivenessPercentage: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            // Usar color del tema con transparencia para mantener consistencia
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = WrestlingTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WrestlingTheme.dimensions.spacing_16)
        ) {
            Text(
                text = "Resumen de Temporada",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Estadísticas principales con mejor visualización
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Luchadas
                StatisticBox(
                    label = "Luchadas",
                    value = totalMatches.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                // Victorias
                StatisticBox(
                    label = "Victorias",
                    value = wins.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.weight(1f)
                )

                // Derrotas
                StatisticBox(
                    label = "Derrotas",
                    value = losses.toString(),
                    color = MaterialTheme.colorScheme.error,
                    backgroundColor = MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.weight(1f)
                )

                // Empates
                StatisticBox(
                    label = "Empates",
                    value = draws.toString(),
                    color = MaterialTheme.colorScheme.tertiary,
                    backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Efectividad - Barra de progreso visual
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Efectividad",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = String.format("%.2f%%", effectivenessPercentage),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = getEffectivenessColor(effectivenessPercentage)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Barra de progreso de efectividad
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(effectivenessPercentage.toFloat() / 100f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(getEffectivenessColor(effectivenessPercentage))
                    )
                }
            }
        }
    }
}

/**
 * Caja de estadística individual con mejor visualización
 */
@Composable
private fun StatisticBox(
    label: String,
    value: String,
    color: Color,
    backgroundColor: Color = color.copy(alpha = 0.1f),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Tabla detallada de estadísticas por clasificación
 */
@Composable
fun DetailedStatisticsTable(
    statisticsByClassification: Map<WrestlerClassification, WrestlerStatistics>
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = WrestlingTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WrestlingTheme.dimensions.spacing_16)
        ) {
            Text(
                text = "Rendimiento por Categoría",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Cabecera de la tabla con mejor contraste y claridad
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    .padding(vertical = 12.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = "Categoría",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1.5f)
                )
                Text(
                    text = "Enfrent.",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Puntos",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Efectividad",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1.2f),
                    textAlign = TextAlign.Center
                )
            }

            // Filas de datos
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f))
            ) {
                // Mostrar las clasificaciones en orden lógico
                WrestlerClassification.getOrderedValues().forEach { classification ->
                    val stats = statisticsByClassification[classification] ?: WrestlerStatistics()

                    if (stats.total > 0) {  // Solo mostrar filas con datos
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = classification.displayName(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1.5f)
                            )
                            Text(
                                text = stats.total.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = String.format("%.1f", stats.points),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            // Porcentaje de efectividad con color según rendimiento
                            Text(
                                text = String.format("%.1f%%", stats.effectivenessPercentage),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = getPerformanceColor(stats.effectivenessPercentage),
                                modifier = Modifier.weight(1.2f),
                                textAlign = TextAlign.Center
                            )
                        }

                        // Añadir divisor excepto para el último elemento
                        if (classification != WrestlerClassification.getOrderedValues().last {
                                (statisticsByClassification[it]?.total ?: 0) > 0
                            }) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Leyenda para mejor comprensión
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                LegendItem(
                    label = "Excelente",
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(16.dp))
                LegendItem(
                    label = "Bueno",
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(16.dp))
                LegendItem(
                    label = "Regular",
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(16.dp))
                LegendItem(
                    label = "Mejorable",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * Elemento de leyenda para la tabla
 */
@Composable
private fun LegendItem(
    label: String,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Desglose de enfrentamientos por tipo de oponente
 */
@Composable
fun OpponentTypeBreakdown(
    puntalesMatches: Int,
    destacadosMatches: Int,
    otherMatches: Int
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = WrestlingTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WrestlingTheme.dimensions.spacing_16)
        ) {
            Text(
                text = "Enfrentamientos por Categoría",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Puntales
                CategoryCountBox(
                    label = "Puntales",
                    count = puntalesMatches,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                VerticalDivider(
                    modifier = Modifier.height(50.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                // Destacados
                CategoryCountBox(
                    label = "Destacados",
                    count = destacadosMatches,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )

                VerticalDivider(
                    modifier = Modifier.height(50.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                // No clasificados
                CategoryCountBox(
                    label = "No Clasificados",
                    count = otherMatches,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Caja de conteo por categoría
 */
@Composable
fun CategoryCountBox(
    label: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Divisor vertical (helper)
 */
@Composable
private fun VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    thickness: Dp = 1.dp
) {
    Box(
        modifier = modifier
            .width(thickness)
            .background(color)
    )
}

/**
 * Devuelve un color basado en el porcentaje de efectividad
 */
@Composable
private fun getEffectivenessColor(percentage: Double): Color {
    return when {
        percentage >= 80.0 -> MaterialTheme.colorScheme.primary
        percentage >= 60.0 -> MaterialTheme.colorScheme.secondary
        percentage >= 40.0 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.error
    }
}

/**
 * Obtiene el color basado en el rendimiento
 */
@Composable
private fun getPerformanceColor(percentage: Double): Color {
    return when {
        percentage >= 80.0 -> MaterialTheme.colorScheme.primary
        percentage >= 60.0 -> MaterialTheme.colorScheme.secondary
        percentage >= 40.0 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.error
    }
}