package dev.baharudin.domain.repository

import androidx.paging.PagingData
import dev.baharudin.common.util.Result
import dev.baharudin.domain.model.Pokemon
import dev.baharudin.domain.model.PokemonDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun getPokemonList(
            searchQuery: String = "",
            selectedTypes: Set<String> = emptySet(),
            sortBy: String = "id_asc"
    ): Flow<List<Pokemon>>

    fun getPokemonListPaged(
            scope: CoroutineScope,
            searchQuery: String = "",
            selectedTypes: Set<String> = emptySet(),
            sortBy: String = "id_asc"
    ): Flow<PagingData<Pokemon>>

    fun getBackpackPokemonPaged(scope: CoroutineScope): Flow<PagingData<Pokemon>>

    suspend fun getPokemonDetail(id: Int): Result<PokemonDetail>

    /**
     * Get Pokemon detail by name.
     *
     * @param name Pokemon name
     * @return Result containing PokemonDetail or error
     */
    suspend fun getPokemonDetail(name: String): Result<PokemonDetail>

    /**
     * Get all Pokemon in backpack (favorites).
     *
     * @return Flow of list of Pokemon in backpack
     */
    fun getBackpackPokemon(): Flow<List<Pokemon>>

    /**
     * Add Pokemon to backpack.
     *
     * @param pokemonId Pokemon ID to add
     * @return Result with success message or error
     */
    suspend fun addPokemonToBackpack(pokemonId: Int): Result<String>

    /**
     * Remove Pokemon from backpack.
     *
     * @param pokemonId Pokemon ID to remove
     * @return Result with success message or error
     */
    suspend fun removePokemonFromBackpack(pokemonId: Int): Result<String>

    /**
     * Check if Pokemon is in backpack.
     *
     * @param pokemonId Pokemon ID
     * @return true if Pokemon is in backpack, false otherwise
     */
    suspend fun isPokemonInBackpack(pokemonId: Int): Boolean

    /**
     * Search Pokemon by name.
     *
     * @param query Search query
     * @return Flow of list of matching Pokemon
     */
    fun searchPokemon(query: String): Flow<List<Pokemon>>

    /**
     * Get all Pokemon types from API.
     *
     * @return Result containing list of type names or error
     */
    suspend fun getAllPokemonTypes(): Result<List<String>>

    /**
     * Mark Pokemon as favorite.
     *
     * @param pokemonId Pokemon ID to mark
     * @param isFavorite true to mark as favorite, false to unmark
     * @return Result with success message or error
     */
    suspend fun updateFavoriteStatus(pokemonId: Int, isFavorite: Boolean): Result<String>

    /**
     * Check if Pokemon is marked as favorite.
     *
     * @param pokemonId Pokemon ID
     * @return true if Pokemon is favorite, false otherwise
     */
    suspend fun isPokemonFavorite(pokemonId: Int): Boolean

    /**
     * Get Flow of favorite Pokemon IDs for real-time updates.
     *
     * @return Flow of Set of Pokemon IDs marked as favorite
     */
    fun getFavoritePokemonIds(): Flow<Set<Int>>

    /**
     * Get Pokemon rating from user data.
     *
     * @param pokemonId Pokemon ID
     * @return Rating value (0-5, where 0 means no rating)
     */
    suspend fun getPokemonRating(pokemonId: Int): Int

    /**
     * Update Pokemon rating.
     *
     * @param pokemonId Pokemon ID to rate
     * @param rating Rating value (0-5, where 0 means no rating)
     * @return Result with success message or error
     */
    suspend fun updateRating(pokemonId: Int, rating: Int): Result<String>

    /**
     * Generate appropriate empty message based on search and filter parameters. This is used by UI
     * layer to show contextual empty states.
     *
     * @param searchQuery Current search query
     * @param selectedTypes Currently selected types
     * @return Appropriate empty message
     */
    fun getEmptyMessage(searchQuery: String, selectedTypes: Set<String>): String
}
