package dev.baharudin.backpack.ui

/** UI state for Backpack screen. */
sealed class BackpackUiState {
    object Initial : BackpackUiState()
    object Loading : BackpackUiState()
    object Success : BackpackUiState()
    data class Error(val message: String) : BackpackUiState()
    object Empty : BackpackUiState()
}

