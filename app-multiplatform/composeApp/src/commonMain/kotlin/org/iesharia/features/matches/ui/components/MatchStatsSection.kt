package org.iesharia.features.matches.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.components.common.InfoRow
import org.iesharia.core.ui.components.common.InfoRowStyle
import org.iesharia.core.ui.components.common.SectionDivider
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.matches.ui.viewmodel.MatchStats

/**
 * Sección de estadísticas del enfrentamiento
 */
@Composable
fun MatchStatsSection(
    stats: MatchStats,
    modifier: Modifier = Modifier,
    localTeamName: String,
    visitorTeamName: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = WrestlingTheme.dimensions.spacing_16)
    ) {
        SectionDivider(title = "Estadísticas")

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

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
                // Resumen de resultados
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stats.localTeamWins.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Victorias $localTeamName",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    VerticalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stats.visitorTeamWins.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Victorias $visitorTeamName",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

                // Detalles del enfrentamiento
                InfoRow(
                    label = "Total de Agarradas",
                    value = stats.totalGrips.toString(),
                    style = InfoRowStyle.HORIZONTAL,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    valueColor = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

                InfoRow(
                    label = "Empates",
                    value = stats.draws.toString(),
                    style = InfoRowStyle.HORIZONTAL,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    valueColor = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

                InfoRow(
                    label = "Separadas",
                    value = stats.separations.toString(),
                    style = InfoRowStyle.HORIZONTAL,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    valueColor = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

                InfoRow(
                    label = "Expulsiones",
                    value = stats.expulsions.toString(),
                    style = InfoRowStyle.HORIZONTAL,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    valueColor = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

                InfoRow(
                    label = "Duración",
                    value = stats.duration,
                    style = InfoRowStyle.HORIZONTAL,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    valueColor = MaterialTheme.colorScheme.onSurface
                )

                if (stats.spectators != null) {
                    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

                    InfoRow(
                        label = "Espectadores",
                        value = stats.spectators.toString(),
                        style = InfoRowStyle.HORIZONTAL,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        valueColor = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun VerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.outlineVariant
) {
    Box(
        modifier = modifier
            .width(thickness)
            .height(48.dp)
            .background(color)
    )
}