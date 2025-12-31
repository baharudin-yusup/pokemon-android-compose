package dev.baharudin.catalog.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import dev.baharudin.catalog.ui.components.CatalogAppBar
import dev.baharudin.catalog.ui.components.FilterSortPanel
import dev.baharudin.common.util.onError
import dev.baharudin.common.util.onSuccess
import dev.baharudin.domain.model.Pokemon
import dev.baharudin.ui.components.PokemonCard
import dev.baharudin.ui.components.PokemonList
import dev.baharudin.ui.util.rememberToastHandler
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
        onPokemonClick: (Pokemon) -> Unit,
        modifier: Modifier = Modifier,
        viewModel: CatalogViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedTypes by viewModel.selectedTypes.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()
    val typesLoadState by viewModel.typesLoadState.collectAsState()
    val favoritePokemonIdSet by viewModel.favoritePokemonIds.collectAsState()
    val pokemonList = viewModel.pokemonList.collectAsLazyPagingItems()

    val coroutineScope = rememberCoroutineScope()
    val showToast = rememberToastHandler()
    var showFilterSort by remember { mutableStateOf(false) }
    val emptyMessage = remember(searchQuery, selectedTypes) { viewModel.getEmptyMessage() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Column(modifier = modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection)) {
        Surface(modifier = Modifier.zIndex(10f)) {
            Column {
                CatalogAppBar(
                        query = searchQuery,
                        onQueryChange = viewModel::updateSearchQuery,
                        onFilterSortClick = { showFilterSort = !showFilterSort },
                        isFilterSortActive = showFilterSort,
                        selectedTypesCount = selectedTypes.size,
                        scrollBehavior = scrollBehavior
                )

                FilterSortPanel(
                        isVisible = showFilterSort,
                        typesLoadState = typesLoadState,
                        selectedTypes = selectedTypes,
                        selectedSort = sortOption,
                        onTypeToggle = viewModel::toggleTypeFilter,
                        onSortChange = viewModel::updateSortOption,
                        onRetryLoadTypes = viewModel::loadPokemonTypes
                )
            }
        }

        PokemonList(
                lazyPagingItems = pokemonList,
                onRetry = { pokemonList.retry() },
                emptyMessage = emptyMessage,
                lazyGridState = viewModel.listState,
                modifier = Modifier.fillMaxSize()
        ) { pokemon, _ ->
            val isFavorite = pokemon.id in favoritePokemonIdSet

            PokemonCard(
                    pokemon = pokemon.copy(isFavorite = isFavorite),
                    onFavoriteClick = {
                        coroutineScope.launch {
                            viewModel
                                    .toggleFavorite(pokemon.id)
                                    .onSuccess { message -> showToast(message) }
                                    .onError { error -> showToast("Failed: ${error.message}") }
                        }
                    },
                    onClick = { onPokemonClick(pokemon) }
            )
        }
    }
}
