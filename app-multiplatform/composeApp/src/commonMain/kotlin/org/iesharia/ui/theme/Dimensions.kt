package org.iesharia.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Dimensiones utilizadas en toda la aplicación
 */
data class Dimensions(
    // Espaciado
    val spacing_2: Dp = 2.dp,
    val spacing_4: Dp = 4.dp,
    val spacing_8: Dp = 8.dp,
    val spacing_12: Dp = 12.dp,
    val spacing_16: Dp = 16.dp,
    val spacing_20: Dp = 20.dp,
    val spacing_24: Dp = 24.dp,
    val spacing_32: Dp = 32.dp,
    val spacing_40: Dp = 40.dp,
    val spacing_48: Dp = 48.dp,
    val spacing_56: Dp = 56.dp,
    val spacing_64: Dp = 64.dp,

    // Elevaciones
    val elevation_small: Dp = 2.dp,
    val elevation_medium: Dp = 4.dp,
    val elevation_large: Dp = 8.dp,

    // Tamaños de componentes
    val icon_size_small: Dp = 16.dp,
    val icon_size_medium: Dp = 24.dp,
    val icon_size_large: Dp = 32.dp,

    // Altura de componentes
    val button_height: Dp = 48.dp,
    val input_field_height: Dp = 56.dp,
    val toolbar_height: Dp = 56.dp,

    // Bordes
    val border_width_small: Dp = 1.dp,
    val border_width_medium: Dp = 2.dp,
    val border_width_large: Dp = 4.dp,

    // Radios de bordes
    val corner_radius_small: Dp = 4.dp,
    val corner_radius_medium: Dp = 8.dp,
    val corner_radius_large: Dp = 16.dp,
    val corner_radius_xlarge: Dp = 24.dp,

    // Específico para login
    val login_logo_size: Dp = 120.dp,
    val login_form_max_width: Dp = 400.dp
)

/**
 * Composition Local para acceder a las dimensiones desde cualquier parte de la app
 */
val LocalAppDimensions = staticCompositionLocalOf { Dimensions() }