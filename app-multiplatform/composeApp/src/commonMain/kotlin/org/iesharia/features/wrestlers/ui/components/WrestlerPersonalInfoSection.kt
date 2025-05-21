package org.iesharia.features.wrestlers.ui.components

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
import kotlinx.datetime.toJavaLocalDate
import org.iesharia.core.ui.components.common.InfoRow
import org.iesharia.core.ui.components.common.InfoRowStyle
import org.iesharia.core.ui.components.common.SectionDivider
import org.iesharia.core.ui.components.common.SectionDividerType
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.wrestlers.domain.model.Wrestler
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/**
 * Sección de información personal del luchador
 */
@Composable
fun WrestlerPersonalInfoSection(
    wrestler: Wrestler,
    teamName: String,
    onTeamClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WrestlingTheme.dimensions.spacing_16)
    ) {
        // Usar SectionDivider con tipo para mantener consistencia
        SectionDivider(
            title = "Información Personal",
            type = SectionDividerType.PRIMARY
        )

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

        // Card con la información personal
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
                // Número de Licencia
                InfoRow(
                    label = "Licencia",
                    value = wrestler.licenseNumber,
                    style = InfoRowStyle.HORIZONTAL,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    valueColor = MaterialTheme.colorScheme.onSurface
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = WrestlingTheme.dimensions.spacing_8),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                // Características físicas en grid de 2x2
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = WrestlingTheme.dimensions.spacing_8),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Altura - usando InfoRow en estilo centrado
                    InfoRow(
                        label = "Altura",
                        value = wrestler.height?.toString()?.plus(" cm") ?: "-",
                        style = InfoRowStyle.CENTERED,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        valueColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    VerticalDivider(
                        modifier = Modifier.height(40.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    // Peso - usando InfoRow en estilo centrado
                    InfoRow(
                        label = "Peso",
                        value = wrestler.weight?.toString()?.plus(" kg") ?: "-",
                        style = InfoRowStyle.CENTERED,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        valueColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = WrestlingTheme.dimensions.spacing_8),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                // Fecha de nacimiento
                InfoRow(
                    label = "Fecha de Nacimiento",
                    value = wrestler.birthDate?.let { formatDate(it) } ?: "-",
                    style = InfoRowStyle.HORIZONTAL,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    valueColor = MaterialTheme.colorScheme.onSurface
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = WrestlingTheme.dimensions.spacing_8),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                // Equipo - MEJORADO para mostrar el nombre del equipo
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = WrestlingTheme.dimensions.spacing_8),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Mostrar "Equipo actual: [Nombre del equipo]"
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Equipo Actual",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = teamName,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Button(
                        onClick = onTeamClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = WrestlingTheme.shapes.small
                    ) {
                        Text("Ver Equipo")
                    }
                }
            }
        }
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
 * Formatea una fecha en formato legible
 */
@Suppress("DEPRECATION")
private fun formatDate(date: kotlinx.datetime.LocalDate): String {
    val javaDate = date.toJavaLocalDate()
    val formatter = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(Locale("es", "ES"))

    return formatter.format(javaDate)
}