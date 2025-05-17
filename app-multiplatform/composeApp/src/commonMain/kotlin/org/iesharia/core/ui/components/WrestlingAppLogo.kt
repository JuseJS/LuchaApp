package org.iesharia.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import org.iesharia.core.ui.theme.WrestlingTheme

/**
 * Logo de la aplicación con tamaño configurable
 */
@Composable
fun WrestlingAppLogo(
    painter: Painter,
    contentDescription: String,
    size: Int = WrestlingTheme.dimensions.login_logo_size.value.toInt()
) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(size = WrestlingTheme.dimensions.login_logo_size)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}