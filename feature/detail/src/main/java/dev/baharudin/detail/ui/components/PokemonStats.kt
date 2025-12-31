package dev.baharudin.detail.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.baharudin.domain.model.PokemonStats

/**
 * Pokemon stats component displaying stats with progress bars.
 *
 * @param stats Pokemon stats
 * @param modifier Modifier for the component
 */
@Composable
fun PokemonStats(stats: PokemonStats, modifier: Modifier = Modifier) {
    Column(
            modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Maximum stat value for progress calculation (255 is max for most Pokemon stats)
        val maxStat = 255

        StatRow(label = "HP", value = stats.hp, maxValue = maxStat)
        StatRow(label = "Attack", value = stats.attack, maxValue = maxStat)
        StatRow(label = "Defense", value = stats.defense, maxValue = maxStat)
        StatRow(label = "Sp. Atk", value = stats.specialAttack, maxValue = maxStat)
        StatRow(label = "Sp. Def", value = stats.specialDefense, maxValue = maxStat)
        StatRow(label = "Speed", value = stats.speed, maxValue = maxStat)
    }
}

@Composable
private fun StatRow(label: String, value: Int, maxValue: Int, modifier: Modifier = Modifier) {
    Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(80.dp)
        )

        LinearProgressIndicator(
                progress = value.toFloat() / maxValue,
                modifier = Modifier.weight(1f).fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Text(
                text = value.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(40.dp)
        )
    }
}
