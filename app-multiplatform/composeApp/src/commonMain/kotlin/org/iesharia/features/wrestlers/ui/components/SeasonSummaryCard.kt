package org.iesharia.features.wrestlers.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.components.common.DataDisplayBox
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.core.ui.util.FormatUtils

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
                // Luchadas - ahora usando DataDisplayBox
                DataDisplayBox(
                    label = "Luchadas",
                    value = totalMatches.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    useCard = true,
                    modifier = Modifier.weight(1f)
                )

                // Victorias - ahora usando DataDisplayBox
                DataDisplayBox(
                    label = "Victorias",
                    value = wins.toString(),
                    color = MaterialTheme.colorScheme.tertiary,
                    backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                    useCard = true,
                    modifier = Modifier.weight(1f)
                )

                // Derrotas - ahora usando DataDisplayBox
                DataDisplayBox(
                    label = "Derrotas",
                    value = losses.toString(),
                    color = MaterialTheme.colorScheme.error,
                    backgroundColor = MaterialTheme.colorScheme.errorContainer,
                    useCard = true,
                    modifier = Modifier.weight(1f)
                )

                // Empates - ahora usando DataDisplayBox
                DataDisplayBox(
                    label = "Empates",
                    value = draws.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    useCard = true,
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
                        color = FormatUtils.Number.getPerformanceColor(effectivenessPercentage)
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
                            .background(FormatUtils.Number.getPerformanceColor(effectivenessPercentage))
                    )
                }
            }
        }
    }
}