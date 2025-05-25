package org.iesharia.features.teams.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.components.FavoriteButton
import org.iesharia.core.ui.theme.DarkSurface2
import org.iesharia.core.ui.theme.White90
import org.iesharia.features.teams.domain.model.Team

@Composable
fun TeamItemWithFavorite(
    team: Team,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = DarkSurface2,
    contentColor: Color = White90,
    showDivision: Boolean = true
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        TeamGridCard(
            team = team,
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            containerColor = containerColor,
            contentColor = contentColor,
            showDivision = showDivision
        )
        
        FavoriteButton(
            isFavorite = isFavorite,
            onToggle = onToggleFavorite,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
        )
    }
}