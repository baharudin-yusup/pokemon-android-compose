package dev.baharudin.detail.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Backpack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Pokemon action buttons (Favorite and Backpack).
 *
 * @param isFavorite Whether Pokemon is marked as favorite
 * @param isInBackpack Whether Pokemon is in backpack
 * @param onToggleFavorite Callback when favorite button is clicked
 * @param onToggleBackpack Callback when backpack button is clicked
 * @param modifier Modifier for the component
 */
@Composable
fun PokemonActions(
    isFavorite: Boolean,
    isInBackpack: Boolean,
    onToggleFavorite: () -> Unit,
    onToggleBackpack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Favorite button
        if (isFavorite) {
            Button(
                onClick = onToggleFavorite,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Remove from favorites",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Unfavorite",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        } else {
            OutlinedButton(
                onClick = onToggleFavorite,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = "Add to favourites",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Favorite",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // Backpack button
        if (isInBackpack) {
            Button(
                onClick = onToggleBackpack,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Backpack,
                    contentDescription = "Remove from backpack",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "In Backpack",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        } else {
            OutlinedButton(
                onClick = onToggleBackpack,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Backpack,
                    contentDescription = "Add to backpack",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Add to Backpack",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

