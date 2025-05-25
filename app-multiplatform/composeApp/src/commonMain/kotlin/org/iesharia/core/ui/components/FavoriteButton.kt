package org.iesharia.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String = if (isFavorite) "Quitar de favoritos" else "Añadir a favoritos"
) {
    val scale by animateFloatAsState(
        targetValue = if (isFavorite) 1.1f else 1f,
        animationSpec = tween(durationMillis = 200)
    )
    
    val tintColor by animateColorAsState(
        targetValue = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 200)
    )
    
    IconButton(
        onClick = onToggle,
        modifier = modifier,
        enabled = enabled
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = contentDescription,
            tint = tintColor,
            modifier = Modifier
                .size(24.dp)
                .scale(scale)
        )
    }
}