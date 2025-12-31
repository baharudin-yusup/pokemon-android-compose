package dev.baharudin.backpack.ui

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.baharudin.common.util.Result
import dev.baharudin.domain.model.Pokemon
import dev.baharudin.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BackpackViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    val backpackPokemon: Flow<PagingData<Pokemon>> =
        repository.getBackpackPokemonPaged(viewModelScope).cachedIn(viewModelScope)

    val listState = LazyGridState()

    val favoritePokemonIds: StateFlow<Set<Int>> = repository.getFavoritePokemonIds()
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    suspend fun toggleFavorite(pokemonId: Int): Result<String> {
        val currentFavorite = repository.isPokemonFavorite(pokemonId)
        return repository.updateFavoriteStatus(pokemonId, !currentFavorite)
    }
}
