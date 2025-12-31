package dev.baharudin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import dev.baharudin.domain.model.Pokemon
import dev.baharudin.ui.theme.getPokemonTypeColor

/**
 * Pokemon card component with square/portrait layout (height > width).
 *
 * Structure (from top to bottom):
 * 1. Pokemon image with favorite button in top-right corner
 * 2. Pokemon name
 * 3. Pokemon types
 *
 * @param pokemon Pokemon data to display (includes isFavorite and rating)
 * @param onFavoriteClick Callback when favorite button is clicked
 * @param onClick Callback when card is clicked
 * @param modifier Modifier for the card
 */
@Composable
fun PokemonCard(
        pokemon: Pokemon,
        modifier: Modifier = Modifier,
        onFavoriteClick: () -> Unit = {},
        onClick: () -> Unit = {}
) {
    val isFavorite = pokemon.isFavorite
    
    Card(
            modifier =
                    modifier.aspectRatio(0.75f) // Height > width (portrait/square)
                            .clickable(onClick = onClick),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
    ) {
        Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Pokemon image with favorite button
            Box(
                    modifier =
                            Modifier.fillMaxWidth()
                                    .aspectRatio(1f)
                                    .background(if (isFavorite) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    contentAlignment = Alignment.TopEnd
            ) {
                // Pokemon image with loading and error states
                SubcomposeAsyncImage(
                        model = pokemon.imageUrl,
                        contentDescription = pokemon.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        loading = {
                            // Loading indicator
                            Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                        modifier = Modifier.size(32.dp),
                                        color = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        error = {
                            // Error message
                            Box(
                                    modifier =
                                            Modifier.fillMaxSize()
                                                    .background(
                                                            MaterialTheme.colorScheme.errorContainer
                                                    ),
                                    contentAlignment = Alignment.Center
                            ) {
                                Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(8.dp)
                                ) {
                                    Text(text = "⚠️", style = MaterialTheme.typography.displaySmall)
                                    Text(
                                            text = "Failed to load image",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onErrorContainer,
                                            textAlign = TextAlign.Center,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                )

                // Favorite button in top-right corner
                IconButton(onClick = onFavoriteClick, modifier = Modifier.padding(8.dp)) {
                    Icon(
                            imageVector =
                                    if (isFavorite) Icons.Default.Favorite
                                    else Icons.Default.FavoriteBorder,
                            contentDescription =
                                    if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint =
                                    if (isFavorite) {
                                        MaterialTheme.colorScheme.error
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    },
                            modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Pokemon name
            Text(
                    text = pokemon.name.replaceFirstChar { it.uppercaseChar() },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            )

            // Pokemon types
            if (pokemon.types.isNotEmpty()) {
                Row(
                        modifier =
                                Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) { pokemon.types.take(2).forEach { type -> TypeBadge(type = type) } }
            }
        }
    }
}

/** Type badge component. */
@Composable
private fun TypeBadge(type: String, modifier: Modifier = Modifier) {
    Box(
            modifier =
                    modifier.clip(RoundedCornerShape(12.dp))
                            .background(getPokemonTypeColor(type))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
    ) {
        Text(
                text = type.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Medium
        )
    }
}
