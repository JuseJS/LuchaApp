package org.iesharia.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.iesharia.domain.model.Wrestler
import org.iesharia.domain.model.WrestlerMatchResult
import org.iesharia.ui.components.common.SectionSubtitle
import org.iesharia.ui.theme.LuchaTheme

/**
 * Item para mostrar un luchador con sus resultados
 */
@Composable
fun WrestlerItem(
    wrestler: Wrestler,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    matchResults: List<WrestlerMatchResult> = emptyList()
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = LuchaTheme.shapes.cardShape,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LuchaTheme.dimensions.spacing_16)
        ) {
            // Cabecera del luchador
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Foto del luchador o placeholder
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    // Corregimos el problema de smartcast verificando primero si imageUrl no es null
                    // y luego verificando si no está vacío como verificaciones separadas
                    val hasValidImage = wrestler.imageUrl != null &&
                            wrestler.imageUrl!!.isNotEmpty()

                    if (hasValidImage) {
                        // Implementar carga de imagen real
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(LuchaTheme.dimensions.spacing_16))

                // Información básica
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = wrestler.fullName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_4))

                    Text(
                        text = "Posición: ${wrestler.position}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Información física
            Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LuchaTheme.dimensions.spacing_12),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Altura
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Altura",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (wrestler.height != null) "${wrestler.height} cm" else "-",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Peso
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Peso",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (wrestler.weight != null) "${wrestler.weight} kg" else "-",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Edad
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Año Nac.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = wrestler.birthYear?.toString() ?: "-",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Resultados de enfrentamientos
            if (matchResults.isNotEmpty()) {
                Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))

                SectionSubtitle(
                    subtitle = "Últimos Enfrentamientos",
                    modifier = Modifier.padding(horizontal = 0.dp)
                )

                matchResults.forEach { result ->
                    WrestlerMatchResultItem(result)
                    Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_8))
                }
            } else if (wrestler.achievements.isNotEmpty()) {
                // Si no hay resultados pero sí logros, mostrarlos
                Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))

                SectionSubtitle(
                    subtitle = "Logros",
                    modifier = Modifier.padding(horizontal = 0.dp)
                )

                wrestler.achievements.forEach { achievement ->
                    Text(
                        text = "• $achievement",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

/**
 * Item que muestra el resultado de un enfrentamiento
 */
@Composable
private fun WrestlerMatchResultItem(result: WrestlerMatchResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LuchaTheme.dimensions.spacing_12)
        ) {
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

                // Chip con el resultado
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = result.result.color().copy(alpha = 0.2f),
                    contentColor = result.result.color()
                ) {
                    Text(
                        text = result.result.displayName(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_8))

            // Oponente
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Contra:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = result.opponentName,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_8))

            // Faltas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Faltas del luchador
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Faltas:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    repeat(result.fouls) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                }

                // Faltas del oponente
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Faltas oponente:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    repeat(result.opponentFouls) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_8))

            // Categoría y clasificación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Categoría",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = result.category,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Clasificación",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = result.classification,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}