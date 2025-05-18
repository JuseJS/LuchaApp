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
 * Niveles de detalle para mostrar un luchador
 */
enum class WrestlerDetailLevel {
    COMPACT,   // Versión mínima con información básica
    STANDARD,  // Información básica + datos adicionales
    DETAILED   // Toda la información con resultados
}

/**
 * Model representing a grip in canarian wrestling
 */
data class Grip(
    val winner: String = "", // Full name of winner
    val isSeparated: Boolean = false // Indicates if the grip was separated
)

/**
 * Item to display a wrestler with different detail levels
 */
@Composable
fun WrestlerItem(
    wrestler: Wrestler,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    matchResults: List<WrestlerMatchResult> = emptyList(),
    detailLevel: WrestlerDetailLevel = WrestlerDetailLevel.STANDARD
) {
    ItemCard(
        onClick = onClick,
        modifier = modifier,
        title = {
            // Wrestler header
            WrestlerHeader(
                wrestler = wrestler,
                detailLevel = detailLevel
            )
        }
    ) {
        // Contenido según nivel de detalle
        when (detailLevel) {
            WrestlerDetailLevel.COMPACT -> {
                // No mostrar contenido adicional
            }
            WrestlerDetailLevel.STANDARD -> {
                // Mostrar información básica
                WrestlerBasicInfo(wrestler)
            }
            WrestlerDetailLevel.DETAILED -> {
                // Mostrar información completa y resultados
                WrestlerBasicInfo(wrestler)

                if (matchResults.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
                    WrestlerResultsSection(wrestler, matchResults)
                }
            }
        }
    }
}

@Composable
private fun WrestlerHeader(
    wrestler: Wrestler,
    detailLevel: WrestlerDetailLevel
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar más grande o más pequeño según el nivel de detalle
        val avatarSize = when(detailLevel) {
            WrestlerDetailLevel.COMPACT -> 40.dp
            else -> 48.dp
        }

        // Wrestler avatar
        Box(
            modifier = Modifier
                .size(avatarSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            val hasValidImage = wrestler.imageUrl != null && wrestler.imageUrl!!.isNotEmpty()
            if (hasValidImage) {
                // Implement real image loading
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(avatarSize * 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.width(WrestlingTheme.dimensions.spacing_16))

        // Wrestler basic info
        Column(modifier = Modifier.weight(1f)) {
            // Full name
            Text(
                text = wrestler.fullName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Nickname (if exists)
            if (wrestler.nickname != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "\"${wrestler.nickname}\"",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Classification
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = wrestler.classification.displayName(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun WrestlerBasicInfo(wrestler: Wrestler) {
    // Additional info row (license, weight, height)
    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // License number
        InfoColumn(
            title = "Licencia",
            value = wrestler.licenseNumber,
            modifier = Modifier.weight(1f)
        )

        // Weight (if exists)
        InfoColumn(
            title = "Peso",
            value = wrestler.weight?.let { "$it kg" } ?: "-",
            modifier = Modifier.weight(1f)
        )

        // Height (if exists)
        InfoColumn(
            title = "Altura",
            value = wrestler.height?.let { "$it cm" } ?: "-",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun WrestlerResultsSection(
    wrestler: Wrestler,
    matchResults: List<WrestlerMatchResult>
) {
    Text(
        text = AppStrings.Wrestlers.recentMatches,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.secondary,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

    // Results list
    matchResults.forEach { result ->
        WrestlerMatchResultView(
            result = result,
            wrestler = wrestler
        )
        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))
    }
}

/**
 * Simple column to display information with title and value
 */
@Composable
private fun InfoColumn(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Component to display a wrestling match
 */
@Composable
fun WrestlerMatchResultView(
    result: WrestlerMatchResult,
    wrestler: Wrestler
) {
    ItemCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentPadding = PaddingValues(WrestlingTheme.dimensions.spacing_12),
        title = {
            // Date and result
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

                // Result badge
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

        // Match between wrestlers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Main wrestler
            WrestlerInfoColumn(
                name = wrestler.fullName,
                classification = wrestler.classification.displayName(),
                fouls = result.fouls,
                modifier = Modifier.weight(1f)
            )

            // Center separator
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

                // Category
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = result.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            // Opponent
            WrestlerInfoColumn(
                name = result.opponentName,
                classification = result.classification,
                fouls = result.opponentFouls,
                modifier = Modifier.weight(1f)
            )
        }

        // Grips section
        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_12))

        Text(
            text = AppStrings.Wrestlers.agarradas,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Get grips from result
        val grips = createGripsFromResult(result, wrestler.fullName)

        // Display grips
        grips.forEachIndexed { index, grip ->
            GripItem(
                grip = grip,
                gripNumber = index + 1
            )

            if (index < grips.size - 1) {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

/**
 * Column displaying wrestler information
 */
@Composable
private fun WrestlerInfoColumn(
    name: String,
    classification: String,
    fouls: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Name
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Classification badge
        InfoBadge(
            text = classification,
            color = MaterialTheme.colorScheme.secondary,
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Fouls badge
        InfoBadge(
            text = AppStrings.Wrestlers.foulsFormat.format(fouls),
            color = if (fouls > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
            backgroundColor = if (fouls > 0)
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

/**
 * Component to display an individual grip
 */
@Composable
private fun GripItem(
    grip: Grip,
    gripNumber: Int
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
            // Grip number
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(24.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "$gripNumber",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Grip information
            if (grip.isSeparated) {
                Text(
                    text = AppStrings.Wrestlers.separated,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Medium
                )
            } else if (grip.winner.isNotEmpty()) {
                Text(
                    text = grip.winner,
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
 * Creates grips from match result
 */
private fun createGripsFromResult(result: WrestlerMatchResult, wrestlerName: String): List<Grip> {
    return when (result.result) {
        WrestlerMatchResult.Result.WIN -> listOf(
            Grip(winner = wrestlerName),
            Grip(winner = result.opponentName),
            Grip(winner = wrestlerName)
        )
        WrestlerMatchResult.Result.LOSS -> listOf(
            Grip(winner = result.opponentName),
            Grip(winner = wrestlerName),
            Grip(winner = result.opponentName)
        )
        WrestlerMatchResult.Result.DRAW -> listOf(
            Grip(winner = wrestlerName),
            Grip(winner = result.opponentName),
            Grip(winner = wrestlerName),
            Grip(winner = result.opponentName)
        )
        WrestlerMatchResult.Result.EXPELLED -> listOf(
            Grip(winner = result.opponentName),
            Grip(isSeparated = true)
        )
        WrestlerMatchResult.Result.SEPARATED -> listOf(
            Grip(winner = wrestlerName),
            Grip(isSeparated = true)
        )
    }
}