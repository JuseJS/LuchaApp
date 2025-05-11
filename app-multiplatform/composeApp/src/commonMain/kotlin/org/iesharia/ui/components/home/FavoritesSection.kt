package org.iesharia.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.iesharia.domain.model.Favorite
import org.iesharia.ui.screens.home.FavoriteType
import org.iesharia.ui.theme.LuchaTheme

/**
 * Sección de favoritos en la pantalla de inicio
 */
@Composable
fun FavoritesSection(
    favorites: List<Favorite>,
    selectedType: FavoriteType,
    onTypeSelected: (FavoriteType) -> Unit,
    onFavoriteClick: (Favorite) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = LuchaTheme.dimensions.spacing_16)
    ) {
        // Título de la sección
        Text(
            text = "Favoritos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = LuchaTheme.dimensions.spacing_16)
        )

        Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_12))

        // Menú para seleccionar tipo de favorito
        FavoriteTypeMenu(
            selectedType = selectedType,
            onTypeSelected = onTypeSelected,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))

        // Lista de favoritos
        if (favorites.isEmpty()) {
            EmptyFavorites(selectedType)
        } else {
            FavoritesList(
                favorites = favorites,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

/**
 * Menú para seleccionar tipo de favorito
 */
@Composable
private fun FavoriteTypeMenu(
    selectedType: FavoriteType,
    onTypeSelected: (FavoriteType) -> Unit,
    modifier: Modifier = Modifier
) {
    val types = FavoriteType.entries.toTypedArray()

    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = LuchaTheme.dimensions.spacing_16)
    ) {
        types.forEach { type ->
            FilterChip(
                selected = type == selectedType,
                onClick = { onTypeSelected(type) },
                label = { Text(type.displayName()) },
                modifier = Modifier.padding(end = LuchaTheme.dimensions.spacing_8)
            )
        }
    }
}

/**
 * Lista horizontal de favoritos
 */
@Composable
private fun FavoritesList(
    favorites: List<Favorite>,
    onFavoriteClick: (Favorite) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = LuchaTheme.dimensions.spacing_16)
    ) {
        favorites.forEach { favorite ->
            FavoriteItem(
                favorite = favorite,
                onClick = { onFavoriteClick(favorite) },
                modifier = Modifier.padding(end = LuchaTheme.dimensions.spacing_12)
            )
        }
    }
}

/**
 * Item para un favorito
 */
@Composable
private fun FavoriteItem(
    favorite: Favorite,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(150.dp)
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = LuchaTheme.shapes.cardShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(LuchaTheme.dimensions.spacing_8),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icono de favorito
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(LuchaTheme.dimensions.spacing_4)
            )

            Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_8))

            // Nombre del favorito
            Text(
                text = when (favorite) {
                    is Favorite.TeamFavorite -> favorite.team.name
                    is Favorite.CompetitionFavorite -> favorite.competition.name
                    is Favorite.WrestlerFavorite -> favorite.wrestler.fullName
                },
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Mensaje para cuando no hay favoritos
 */
@Composable
private fun EmptyFavorites(selectedType: FavoriteType) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No tienes ${selectedType.displayName().lowercase()} en favoritos",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}