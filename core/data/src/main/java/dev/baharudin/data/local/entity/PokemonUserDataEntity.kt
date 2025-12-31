package dev.baharudin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for Pokemon user-specific data (separate table).
 * 
 * This table stores user-specific state that's independent from the main
 * pokemon list (catalog/backpack). Changes to this table do NOT trigger
 * PagingSource invalidation, eliminating flicker.
 * 
 * Consolidates:
 * - Favorite status
 * - Rating (0-5 stars)
 * 
 * Only entries with non-default values are stored to save space.
 */
@Entity(tableName = "pokemon_user_data")
internal data class PokemonUserDataEntity(
    @PrimaryKey
    val pokemonId: Int,
    val isFavorite: Boolean = false,
    val rating: Int = 0 // 0-5 stars, 0 means not rated
)

