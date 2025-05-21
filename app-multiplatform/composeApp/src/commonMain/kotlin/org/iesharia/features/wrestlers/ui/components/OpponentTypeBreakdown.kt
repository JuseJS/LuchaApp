package org.iesharia.features.wrestlers.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.components.common.VerticalDivider
import org.iesharia.core.ui.theme.WrestlingTheme

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
                    height = 50.dp,
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
                    height = 50.dp,
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