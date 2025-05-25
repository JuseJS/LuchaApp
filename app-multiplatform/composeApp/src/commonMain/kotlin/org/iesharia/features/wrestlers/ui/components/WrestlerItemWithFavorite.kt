package org.iesharia.features.wrestlers.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.components.FavoriteButton
import org.iesharia.features.wrestlers.domain.model.Wrestler

@Composable
fun WrestlerItemWithFavorite(
    wrestler: Wrestler,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        WrestlerItem(
            wrestler = wrestler,
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
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