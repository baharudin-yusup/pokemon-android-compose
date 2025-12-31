package dev.baharudin.domain.model

/**
 * Domain model for Pokemon (list item).
 *
 * This represents a Pokemon in the catalog/list view.
 */
data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val types: List<String>,
    val isFavorite: Boolean = false,
    val rating: Int = 0 // 0-5 stars, 0 means not rated
)

