package org.iesharia.features.wrestlers.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.common.*
import org.iesharia.core.ui.theme.*
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
 * Item optimizado para mostrar un luchador usando componentes reutilizables
 */
@Composable
fun WrestlerItem(
    wrestler: Wrestler,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    matchResults: List<WrestlerMatchResult> = emptyList(),
    detailLevel: WrestlerDetailLevel = WrestlerDetailLevel.STANDARD,
    isGridItem: Boolean = false
) {
    EntityListItem(
        onClick = onClick,
        modifier = modifier,
        containerColor = DarkSurface2,
        leadingContent = {
            AvatarBox(
                size = 48.dp,
                avatarType = AvatarType.WRESTLER,
                imageUrl = wrestler.imageUrl,
                fallbackText = wrestler.fullName,
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        titleContent = {
            WrestlerTitleContent(wrestler, isGridItem)
        },
        trailingContent = if (isGridItem) {
            {
                InfoBadge(
                    text = wrestler.classification.displayName(),
                    color = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                )
            }
        } else null,
        detailContent = if (detailLevel != WrestlerDetailLevel.COMPACT && !isGridItem) {
            {
                WrestlerDetailContent(
                    wrestler = wrestler,
                    matchResults = matchResults,
                    detailLevel = detailLevel
                )
            }
        } else null
    )
}

@Composable
private fun WrestlerTitleContent(wrestler: Wrestler, isGridItem: Boolean) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(
            text = wrestler.fullName,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = White90,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        wrestler.nickname?.let { nickname ->
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "\"$nickname\"",
                style = MaterialTheme.typography.bodySmall,
                color = SandLight,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (!isGridItem) {
            Spacer(modifier = Modifier.height(8.dp))
            InfoBadge(
                text = wrestler.classification.displayName(),
                color = MaterialTheme.colorScheme.primary,
                backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            )
        }
    }
}

@Composable
private fun WrestlerDetailContent(
    wrestler: Wrestler,
    matchResults: List<WrestlerMatchResult>,
    detailLevel: WrestlerDetailLevel
) {
    if (detailLevel == WrestlerDetailLevel.STANDARD) {
        WrestlerBasicInfo(wrestler)
    } else if (detailLevel == WrestlerDetailLevel.DETAILED) {
        WrestlerBasicInfo(wrestler)
        if (matchResults.isNotEmpty()) {
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
            WrestlerResultsSection(wrestler, matchResults)
        }
    }
}

@Composable
private fun WrestlerBasicInfo(wrestler: Wrestler) {
    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        InfoRow(
            label = "Licencia",
            value = wrestler.licenseNumber,
            style = InfoRowStyle.CENTERED,
            labelStyle = MaterialTheme.typography.labelSmall,
            valueStyle = MaterialTheme.typography.bodySmall,
            labelColor = White80,
            valueColor = White90,
            modifier = Modifier.weight(1f)
        )

        InfoRow(
            label = "Peso",
            value = wrestler.weight?.let { "$it kg" } ?: "-",
            style = InfoRowStyle.CENTERED,
            labelStyle = MaterialTheme.typography.labelSmall,
            valueStyle = MaterialTheme.typography.bodySmall,
            labelColor = White80,
            valueColor = White90,
            modifier = Modifier.weight(1f)
        )

        InfoRow(
            label = "Altura",
            value = wrestler.height?.let { "$it cm" } ?: "-",
            style = InfoRowStyle.CENTERED,
            labelStyle = MaterialTheme.typography.labelSmall,
            valueStyle = MaterialTheme.typography.bodySmall,
            labelColor = White80,
            valueColor = White90,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun WrestlerResultsSection(
    wrestler: Wrestler,
    matchResults: List<WrestlerMatchResult>
) {
    SectionDivider(
        title = AppStrings.Wrestlers.recentMatches,
        type = SectionDividerType.SUBTITLE,
        textColor = CanaryBlueLight
    )

    matchResults.forEach { result ->
        WrestlerMatchResultCard(result = result, wrestler = wrestler)
        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))
    }
}

/**
 * Tarjeta de resultado de enfrentamiento optimizada usando EntityListItem
 */
@Composable
fun WrestlerMatchResultCard(
    result: WrestlerMatchResult,
    wrestler: Wrestler
) {
    EntityListItem(
        onClick = { /* No acción en este caso */ },
        modifier = Modifier.fillMaxWidth(),
        containerColor = DarkSurface2,
        titleContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = result.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = White80
                )

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
        },
        detailContent = {
            WrestlerMatchResultDetail(result, wrestler)
        }
    )
}

@Composable
private fun WrestlerMatchResultDetail(
    result: WrestlerMatchResult,
    wrestler: Wrestler
) {
    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_12))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WrestlerInfoColumn(
            name = wrestler.fullName,
            classification = wrestler.classification.displayName(),
            fouls = result.fouls,
            modifier = Modifier.weight(1f)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = AppStrings.Wrestlers.vs,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = CanaryBlueLight
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = result.category,
                style = MaterialTheme.typography.labelSmall,
                color = White80,
                textAlign = TextAlign.Center
            )
        }

        WrestlerInfoColumn(
            name = result.opponentName,
            classification = result.classification,
            fouls = result.opponentFouls,
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_12))

    SectionDivider(
        title = AppStrings.Wrestlers.agarradas,
        type = SectionDividerType.SUBTITLE,
        textColor = SandLight
    )

    val grips = createGripsFromResult(result, wrestler.fullName)
    grips.forEachIndexed { index, grip ->
        GripDisplayCard(
            gripNumber = index + 1,
            winnerName = grip.winner,
            isSeparated = grip.isSeparated
        )

        if (index < grips.size - 1) {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

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
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            color = White90
        )

        Spacer(modifier = Modifier.height(4.dp))

        InfoBadge(
            text = classification,
            color = CanaryBlueLight,
            backgroundColor = DarkSurface3
        )

        Spacer(modifier = Modifier.height(4.dp))

        InfoBadge(
            text = AppStrings.Wrestlers.foulsFormat.format(fouls),
            color = if (fouls > 0) TraditionalRedLight else White80,
            backgroundColor = if (fouls > 0)
                TraditionalRed.copy(alpha = 0.3f)
            else
                DarkSurface3
        )
    }
}

@Composable
private fun GripDisplayCard(
    gripNumber: Int,
    winnerName: String,
    isSeparated: Boolean
) {
    ItemCard(
        onClick = null,
        containerColor = DarkSurface2,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AvatarBox(
                    size = 24.dp,
                    fallbackText = gripNumber.toString(),
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = when {
                        isSeparated -> AppStrings.Wrestlers.separated
                        winnerName.isNotEmpty() -> winnerName
                        else -> AppStrings.Wrestlers.noResult
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = when {
                        isSeparated -> LaurisilvaGreenLight
                        winnerName.isNotEmpty() -> White90
                        else -> White80
                    }
                )
            }
        }
    )
}

/**
 * Modelo para representar una agarrada
 */
data class Grip(
    val winner: String = "",
    val isSeparated: Boolean = false
)

/**
 * Crea agarradas a partir del resultado del enfrentamiento
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