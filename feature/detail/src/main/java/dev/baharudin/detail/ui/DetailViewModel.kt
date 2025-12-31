package dev.baharudin.detail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.baharudin.common.util.Result
import dev.baharudin.domain.repository.PokemonRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: PokemonRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Initial)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val _isInBackpack = MutableStateFlow(false)
    val isInBackpack: StateFlow<Boolean> = _isInBackpack.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _rating = MutableStateFlow(0)
    val rating: StateFlow<Int> = _rating.asStateFlow()

    fun loadPokemonDetail(pokemonId: Int) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading

            val detailResult = repository.getPokemonDetail(pokemonId)

            if (detailResult is Result.Success) {
                _isInBackpack.value = repository.isPokemonInBackpack(pokemonId)
                _isFavorite.value = repository.isPokemonFavorite(pokemonId)
                _rating.value = repository.getPokemonRating(pokemonId)
            }

            when (detailResult) {
                is Result.Success -> {
                    _uiState.value = DetailUiState.Success(detailResult.data)
                }
                is Result.Error -> {
                    _uiState.value =
                            DetailUiState.Error(
                                    detailResult.exception.message
                                            ?: "Failed to load Pokemon detail"
                            )
                }
                is Result.Loading -> {}
            }
        }
    }

    fun loadPokemonDetail(pokemonName: String) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading

            val detailResult = repository.getPokemonDetail(pokemonName)

            if (detailResult is Result.Success) {
                val pokemonId = detailResult.data.id
                _isInBackpack.value = repository.isPokemonInBackpack(pokemonId)
                _isFavorite.value = repository.isPokemonFavorite(pokemonId)
                _rating.value = repository.getPokemonRating(pokemonId)
            }

            when (detailResult) {
                is Result.Success -> {
                    _uiState.value = DetailUiState.Success(detailResult.data)
                }
                is Result.Error -> {
                    _uiState.value =
                            DetailUiState.Error(
                                    detailResult.exception.message
                                            ?: "Failed to load Pokemon detail"
                            )
                }
                is Result.Loading -> {}
            }
        }
    }

    fun toggleBackpackStatus(pokemonId: Int) {
        viewModelScope.launch {
            val isCurrentlyInBackpack = _isInBackpack.value

            val result =
                    if (isCurrentlyInBackpack) {
                        repository.removePokemonFromBackpack(pokemonId)
                    } else {
                        repository.addPokemonToBackpack(pokemonId)
                    }

            if (result is Result.Success) {
                _isInBackpack.value = !isCurrentlyInBackpack
            }
        }
    }

    suspend fun toggleFavorite(pokemonId: Int): Result<String> {
        val currentFavoriteStatus = _isFavorite.value
        val result = repository.updateFavoriteStatus(pokemonId, !currentFavoriteStatus)

        if (result is Result.Success) {
            _isFavorite.value = !currentFavoriteStatus
        }

        return result
    }

    suspend fun toggleBackpack(pokemonId: Int): Result<String> {
        val isCurrentlyInBackpack = _isInBackpack.value

        val result =
                if (isCurrentlyInBackpack) {
                    repository.removePokemonFromBackpack(pokemonId)
                } else {
                    repository.addPokemonToBackpack(pokemonId)
                }

        if (result is Result.Success) {
            _isInBackpack.value = !isCurrentlyInBackpack
        }

        return result
    }

    suspend fun updateRating(pokemonId: Int, rating: Int): Result<String> {
        val result = repository.updateRating(pokemonId, rating)

        if (result is Result.Success) {
            _rating.value = rating
        }

        return result
    }

    fun clearError() {
        if (_uiState.value is DetailUiState.Error) {
            val currentDetail = (_uiState.value as? DetailUiState.Success)?.pokemonDetail
            if (currentDetail != null) {
                _uiState.value = DetailUiState.Success(currentDetail)
            } else {
                _uiState.value = DetailUiState.Initial
            }
        }
    }
}
