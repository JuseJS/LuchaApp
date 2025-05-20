package org.iesharia.features.matches.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.components.common.EmptyStateMessage
import org.iesharia.core.ui.components.common.SectionDivider
import org.iesharia.core.ui.components.common.SectionDividerType
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.wrestlers.domain.model.WrestlerCategory
import org.iesharia.features.wrestlers.domain.model.WrestlerClassification

/**
 * Sección que muestra la alineación de un equipo en el enfrentamiento
 */
@Composable
fun TeamLineupSection(
    teamName: String,
    wrestlers: List<Wrestler>,
    isHome: Boolean,
    onWrestlerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    titleColor: Color = if (isHome) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = WrestlingTheme.dimensions.spacing_16)
    ) {
        // Título de la sección
        SectionDivider(
            title = "Alineación $teamName",
            textColor = titleColor
        )

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_12))

        if (wrestlers.isEmpty()) {
            EmptyStateMessage(
                message = "No hay luchadores disponibles",
                height = 100.dp
            )
        } else {
            // Agrupar por categoría
            val wrestlersByCategory = wrestlers.groupBy { it.category }

            // REGIONALES
            WrestlerCategoryLineup(
                category = WrestlerCategory.REGIONAL,
                wrestlers = wrestlersByCategory[WrestlerCategory.REGIONAL] ?: emptyList(),
                isHome = isHome,
                onWrestlerClick = onWrestlerClick
            )

            // JUVENILES
            if (wrestlersByCategory.containsKey(WrestlerCategory.JUVENIL)) {
                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
                WrestlerCategoryLineup(
                    category = WrestlerCategory.JUVENIL,
                    wrestlers = wrestlersByCategory[WrestlerCategory.JUVENIL] ?: emptyList(),
                    isHome = isHome,
                    onWrestlerClick = onWrestlerClick
                )
            }

            // CADETES
            if (wrestlersByCategory.containsKey(WrestlerCategory.CADETE)) {
                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
                WrestlerCategoryLineup(
                    category = WrestlerCategory.CADETE,
                    wrestlers = wrestlersByCategory[WrestlerCategory.CADETE] ?: emptyList(),
                    isHome = isHome,
                    onWrestlerClick = onWrestlerClick
                )
            }
        }
    }
}

/**
 * Muestra la alineación de luchadores para una categoría específica
 */
@Composable
private fun WrestlerCategoryLineup(
    category: WrestlerCategory,
    wrestlers: List<Wrestler>,
    isHome: Boolean,
    onWrestlerClick: (String) -> Unit
) {
    val sortedWrestlers = when (category) {
        WrestlerCategory.REGIONAL -> {
            // Ordenar regionales por clasificación
            wrestlers.sortedBy { wrestler ->
                WrestlerClassification.getOrderedValues().indexOf(wrestler.classification)
            }
        }
        else -> wrestlers
    }

    Column {
        // Título de la categoría
        SectionDivider(
            title = category.displayName(),
            type = SectionDividerType.SUBTITLE,
            textColor = if (isHome) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

        // Lista de luchadores
        sortedWrestlers.forEach { wrestler ->
            WrestlerLineupCard(
                wrestler = wrestler,
                onClick = { onWrestlerClick(wrestler.id) },
                isHome = isHome,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}