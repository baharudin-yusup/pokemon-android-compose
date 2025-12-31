package dev.baharudin.pokemon_compose.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Backpack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import dev.baharudin.backpack.ui.BackpackScreen
import dev.baharudin.catalog.ui.CatalogScreen
import dev.baharudin.domain.model.Pokemon

@Composable
fun HomeScreen(
    onPokemonClick: (Pokemon) -> Unit,
    modifier: Modifier = Modifier,
    initialDestination: AppDestinations = AppDestinations.CATALOG
) {
    var currentDestination by rememberSaveable { mutableStateOf(initialDestination) }
    NavigationSuiteScaffold(
        modifier = modifier.fillMaxSize(),
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                val isSelected = destination == currentDestination
                item(
                    icon = {
                        Icon(
                            if (isSelected) destination.selectedIcon
                            else destination.icon,
                            contentDescription = destination.label
                        )
                    },
                    label = { Text(destination.label) },
                    selected = isSelected,
                    onClick = { currentDestination = destination }
                )
            }
        }
    ) {
        when (currentDestination) {
            AppDestinations.CATALOG -> CatalogScreen(onPokemonClick = onPokemonClick)
            AppDestinations.BACKPACK -> BackpackScreen(onPokemonClick = onPokemonClick)
        }
    }
}


enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    CATALOG("Catalog", Icons.Outlined.Home, Icons.Default.Home),
    BACKPACK("Backpack", Icons.Outlined.Backpack, Icons.Default.Backpack),
}