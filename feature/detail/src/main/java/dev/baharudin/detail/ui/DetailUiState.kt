package dev.baharudin.detail.ui

import dev.baharudin.domain.model.PokemonDetail

/**
 * UI state for Detail screen.
 */
sealed class DetailUiState {
    object Initial : DetailUiState()
    object Loading : DetailUiState()
    data class Success(val pokemonDetail: PokemonDetail) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}
