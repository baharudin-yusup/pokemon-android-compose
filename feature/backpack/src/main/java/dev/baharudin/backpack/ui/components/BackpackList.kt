package dev.baharudin.backpack.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.baharudin.domain.model.Pokemon
import dev.baharudin.ui.components.EmptyState
import dev.baharudin.ui.components.PokemonCard

/**
 * Backpack list component displaying saved Pokemon in a 2-column grid.
 *
 * This component is UI-focused: receives data and callbacks as parameters, no ViewModel.
 *
 * @param pokemonList List of Pokemon in backpack
 * @param onPokemonClick Callback when Pokemon is clicked
 * @param onRemoveClick Callback when remove/favorite button is clicked
 * @param modifier Modifier for the list
 */
@Composable
fun BackpackList(
        pokemonList: List<Pokemon>,
        onPokemonClick: (Pokemon) -> Unit,
        onRemoveClick: (pokemonId: Int) -> Unit,
        modifier: Modifier = Modifier
) {
    if (pokemonList.isEmpty()) {
        EmptyState(
                message = "Your backpack is empty. Add Pokemon from the catalog!",
                modifier = modifier
        )
    } else {
        LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                    items = pokemonList,
                    key = { pokemon -> pokemon.id }
            ) { pokemon ->
                PokemonCard(
                        pokemon = pokemon,
                        onFavoriteClick = { onRemoveClick(pokemon.id) },
                        onClick = { onPokemonClick(pokemon) }
                )
            }
        }
    }
}

