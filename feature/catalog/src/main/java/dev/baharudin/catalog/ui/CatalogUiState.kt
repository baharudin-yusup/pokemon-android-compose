package dev.baharudin.catalog.ui

sealed class TypesLoadState {
    data object Loading : TypesLoadState()
    data class Success(val types: List<String>) : TypesLoadState()
    data class Error(val message: String) : TypesLoadState()
}

