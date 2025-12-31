package dev.baharudin.detail.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.baharudin.ui.theme.getPokemonTypeColor

/**
 * Pokemon types component showing type badges.
 *
 * @param types List of Pokemon types
 * @param modifier Modifier for the component
 */
@Composable
fun PokemonTypes(types: List<String>, modifier: Modifier = Modifier) {
    if (types.isEmpty()) return

    Row(
            modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        types.forEach { type ->
            TypeBadge(type = type)
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
                            .padding(horizontal = 12.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
    ) {
        Text(
                text = type.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Medium
        )
    }
}
