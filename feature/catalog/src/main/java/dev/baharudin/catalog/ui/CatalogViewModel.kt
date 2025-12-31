package dev.baharudin.catalog.ui

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.baharudin.catalog.ui.components.SortOption
import dev.baharudin.domain.model.Pokemon
import dev.baharudin.domain.repository.PokemonRepository
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import dev.baharudin.common.util.Result

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CatalogViewModel @Inject constructor(private val repository: PokemonRepository) :
        ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedTypes = MutableStateFlow<Set<String>>(emptySet())
    val selectedTypes: StateFlow<Set<String>> = _selectedTypes.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.ID_ASC)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val _typesLoadState = MutableStateFlow<TypesLoadState>(TypesLoadState.Loading)
    val typesLoadState: StateFlow<TypesLoadState> = _typesLoadState.asStateFlow()

    val listState = LazyGridState()

    val favoritePokemonIds: StateFlow<Set<Int>> =
            repository
                    .getFavoritePokemonIds()
                    .stateIn(
                            scope = viewModelScope,
                            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
                            initialValue = emptySet()
                    )

    val pokemonList: Flow<PagingData<Pokemon>> =
            combine(_searchQuery, _selectedTypes, _sortOption) { query, types, sort ->
                        val sortBy =
                                when (sort) {
                                    SortOption.NAME_ASC -> "name_asc"
                                    SortOption.NAME_DESC -> "name_desc"
                                    SortOption.ID_ASC -> "id_asc"
                                    SortOption.ID_DESC -> "id_desc"
                                    SortOption.TYPE -> "type"
                                }

                        repository.getPokemonListPaged(
                                scope = viewModelScope,
                                searchQuery = query,
                                selectedTypes = types,
                                sortBy = sortBy
                        )
                    }
                    .flatMapLatest { it }
                    .cachedIn(viewModelScope)

    init {
        loadPokemonTypes()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleTypeFilter(type: String) {
        _selectedTypes.value =
                if (type in _selectedTypes.value) {
                    _selectedTypes.value - type
                } else {
                    _selectedTypes.value + type
                }
    }

    fun updateSortOption(option: SortOption) {
        _sortOption.value = option
    }

    suspend fun toggleFavorite(pokemonId: Int): Result<String> {
        val currentFavorite = repository.isPokemonFavorite(pokemonId)
        return repository.updateFavoriteStatus(pokemonId, !currentFavorite)
    }

    fun getEmptyMessage(): String {
        return repository.getEmptyMessage(
                searchQuery = _searchQuery.value,
                selectedTypes = _selectedTypes.value
        )
    }

    fun loadPokemonTypes() {
        viewModelScope.launch {
            _typesLoadState.value = TypesLoadState.Loading
            when (val result = repository.getAllPokemonTypes()) {
                is Result.Success -> {
                    _typesLoadState.value = TypesLoadState.Success(result.data)
                }
                is Result.Error -> {
                    _typesLoadState.value =
                            TypesLoadState.Error(result.exception.message ?: "Failed to load types")
                }
                is Result.Loading -> {
                    _typesLoadState.value = TypesLoadState.Loading
                }
            }
        }
    }
}
