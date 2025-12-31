package dev.baharudin.backpack.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import dev.baharudin.common.util.onError
import dev.baharudin.common.util.onSuccess
import dev.baharudin.domain.model.Pokemon
import dev.baharudin.ui.components.PokemonCard
import dev.baharudin.ui.components.PokemonList
import dev.baharudin.ui.util.rememberToastHandler
import kotlinx.coroutines.launch

/**
 * Main backpack screen for displaying saved Pokemon collection.
 *
 * Features:
 * - Display all Pokemon in backpack using Paging 3 (consistent with Catalog)
 * - Remove Pokemon from backpack
 * - Navigate to detail screen
 * - Empty state when backpack is empty
 * - Loading states handled automatically by Paging 3 (no manual state management)
 *
 * @param onPokemonClick Callback when Pokemon is clicked (for navigation)
 * @param modifier Modifier for the screen
 * @param viewModel BackpackViewModel injected via Hilt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackpackScreen(
    onPokemonClick: (Pokemon) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BackpackViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val showToast = rememberToastHandler()

    val backpackPokemon = viewModel.backpackPokemon.collectAsLazyPagingItems()
    val favoritePokemonIds by viewModel.favoritePokemonIds.collectAsState()

    // Setup scroll behavior for TopAppBar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Backpack") },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        // Pokemon list
        PokemonList(
            lazyPagingItems = backpackPokemon,
            onRetry = { backpackPokemon.retry() },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            emptyMessage = "Your backpack is empty. Add Pokemon from the catalog!",
            lazyGridState = viewModel.listState
        ) { pokemon, _ ->
            val isFavorite = pokemon.id in favoritePokemonIds
            
            PokemonCard(
                pokemon = pokemon.copy(isFavorite = isFavorite),
                onFavoriteClick = {
                    coroutineScope.launch {
                        viewModel.toggleFavorite(pokemon.id)
                            .onSuccess { message -> showToast(message) }
                            .onError { error -> showToast("Failed: ${error.message}") }
                    }
                },
                onClick = { onPokemonClick(pokemon) }
            )
        }
    }
}
