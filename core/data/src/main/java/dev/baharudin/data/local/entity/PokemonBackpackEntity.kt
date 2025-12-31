package dev.baharudin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for Pokemon backpack tracking (ID-only table).
 * 
 * This table only stores which Pokemon are in the backpack.
 * Actual Pokemon data is fetched via JOIN with pokemon table.
 * 
 * Simpler and more normalized approach.
 */
@Entity(tableName = "pokemon_backpack")
internal data class PokemonBackpackEntity(
    @PrimaryKey
    val pokemonId: Int,
    val addedAt: Long = System.currentTimeMillis() // Timestamp when added
)

