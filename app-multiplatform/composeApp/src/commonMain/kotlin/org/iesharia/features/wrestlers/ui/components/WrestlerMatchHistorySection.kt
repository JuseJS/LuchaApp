package org.iesharia.features.wrestlers.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.common.*
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult

/**
 * Sección de historial de enfrentamientos optimizada usando componentes reutilizables
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
        SectionDivider(
            title = AppStrings.Wrestlers.recentMatches,
            type = SectionDividerType.PRIMARY
        )

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

        if (matchResults.isEmpty()) {
            EmptyStateMessage(message = "No hay enfrentamientos registrados")
        } else {
            matchResults.forEach { result ->
                WrestlerMatchResultCard(result = result)
                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))
            }
        }
    }
}

/**
 * Tarjeta de resultado de enfrentamiento optimizada usando EntityListItem
 */
@Composable
fun WrestlerMatchResultCard(
    result: WrestlerMatchResult
) {
    EntityListItem(
        onClick = { /* No acción en este caso */ },
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        leadingContent = {
            AvatarBox(
                size = 48.dp,
                avatarType = AvatarType.WRESTLER,
                fallbackText = result.opponentName,
                backgroundColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                contentColor = MaterialTheme.colorScheme.secondary
            )
        },
        titleContent = {
            Text(
                text = result.opponentName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        subtitleContent = {
            Text(
                text = result.classification,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingContent = {
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
        },
        detailContent = {
            WrestlerMatchResultDetailContent(result)
        }
    )
}

@Composable
private fun WrestlerMatchResultDetailContent(result: WrestlerMatchResult) {
    if (result.result != WrestlerMatchResult.Result.SEPARATED &&
        result.result != WrestlerMatchResult.Result.EXPELLED) {

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_12))

        SectionDivider(
            title = AppStrings.Wrestlers.agarradas,
            type = SectionDividerType.SUBTITLE,
            textColor = MaterialTheme.colorScheme.onSurfaceVariant
        )

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

    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_12))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
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

/**
 * Elemento individual de agarrada usando ItemCard
 */
@Composable
private fun GripItem(
    gripNumber: Int,
    isWin: Boolean,
    isSeparated: Boolean
) {
    ItemCard(
        onClick = null,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                AvatarBox(
                    size = 24.dp,
                    fallbackText = "$gripNumber",
                    backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    contentColor = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

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
    )
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