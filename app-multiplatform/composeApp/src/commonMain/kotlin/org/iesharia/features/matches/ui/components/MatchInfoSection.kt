package org.iesharia.features.matches.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.iesharia.features.teams.domain.model.Match

/**
 * Sección de información con fecha, hora y lugar del enfrentamiento
 */
@Composable
fun MatchInfoSection(
    match: Match,
    modifier: Modifier = Modifier
) {
    // Format date without deprecated method
    val dateTime = match.date
    val formattedDate = "${dateTime.dayOfMonth.toString().padStart(2, '0')}/${dateTime.monthNumber.toString().padStart(2, '0')}/${dateTime.year}"
    val formattedTime = "${dateTime.hour.toString().padStart(2, '0')}:${dateTime.minute.toString().padStart(2, '0')}"

    Column(modifier = modifier) {
        // Fecha y hora del enfrentamiento
        MatchInfoRow(
            icon = Icons.Default.Schedule,
            text = "$formattedDate - $formattedTime"
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Lugar del enfrentamiento
        MatchInfoRow(
            icon = Icons.Default.Place,
            text = match.venue
        )
    }
}

/**
 * Fila de información con icono y texto para información del enfrentamiento
 */
@Composable
private fun MatchInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}