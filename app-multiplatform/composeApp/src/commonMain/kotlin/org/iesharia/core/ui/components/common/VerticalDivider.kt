package org.iesharia.core.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Divisor vertical reutilizable en toda la aplicación
 */
@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    thickness: Dp = 1.dp,
    height: Dp? = null
) {
    val modifierWithHeight = if (height != null) {
        modifier.height(height)
    } else {
        modifier
    }

    Box(
        modifier = modifierWithHeight
            .width(thickness)
            .background(color)
    )
}