package dev.baharudin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import dev.baharudin.data.local.converter.Converters

/**
 * Pokemon entity for Room database.
 *
 * This entity represents a Pokemon in the local database with caching information.
 */
@Entity(tableName = "pokemon")
@TypeConverters(Converters::class)
internal data class PokemonEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val height: Int,
    val weight: Int,
    val types: List<String>,
    val hp: Int?,
    val attack: Int?,
    val defense: Int?,
    val specialAttack: Int?,
    val specialDefense: Int?,
    val speed: Int?,
    val abilities: List<String>,
    val isInBackpack: Boolean = false,
    val isFavorite: Boolean = false,
    val rating: Int = 0 // 0-5 stars, 0 means not rated
)

/**
 * Remote keys for Paging 3 RemoteMediator.
 * Used to track pagination state.
 */
@Entity(tableName = "pokemon_remote_keys")
internal data class PokemonRemoteKeysEntity(
    @PrimaryKey
    val pokemonId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)

