package dev.baharudin.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.baharudin.data.local.entity.PokemonUserDataEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Pokemon user data operations (favorite, rating).
 * 
 * This table is separate from the main pokemon table to prevent
 * PagingSource invalidation when user state changes.
 */
@Dao
internal interface PokemonUserDataDao {
    
    /**
     * Get user data for a specific Pokemon.
     */
    @Query("SELECT * FROM pokemon_user_data WHERE pokemonId = :pokemonId")
    suspend fun getUserData(pokemonId: Int): PokemonUserDataEntity?
    
    /**
     * Observe all favorite Pokemon IDs.
     */
    @Query("SELECT pokemonId FROM pokemon_user_data WHERE isFavorite = 1")
    fun observeFavoritePokemonIds(): Flow<List<Int>>
    
    /**
     * Check if a Pokemon is favorited.
     */
    @Query("SELECT isFavorite FROM pokemon_user_data WHERE pokemonId = :pokemonId")
    suspend fun isFavorite(pokemonId: Int): Boolean?
    
    /**
     * Get rating for a Pokemon.
     */
    @Query("SELECT rating FROM pokemon_user_data WHERE pokemonId = :pokemonId")
    suspend fun getRating(pokemonId: Int): Int?
    
    /**
     * Insert or update user data.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(userData: PokemonUserDataEntity)
    
    /**
     * Update favorite status.
     * Creates entry if doesn't exist.
     */
    @Transaction
    suspend fun updateFavorite(pokemonId: Int, isFavorite: Boolean) {
        val existing = getUserData(pokemonId)
        if (existing != null) {
            insertOrUpdate(existing.copy(isFavorite = isFavorite))
        } else {
            insertOrUpdate(PokemonUserDataEntity(pokemonId, isFavorite = isFavorite))
        }
    }
    
    /**
     * Update rating.
     * Creates entry if doesn't exist.
     */
    @Transaction
    suspend fun updateRating(pokemonId: Int, rating: Int) {
        val existing = getUserData(pokemonId)
        if (existing != null) {
            insertOrUpdate(existing.copy(rating = rating))
        } else {
            insertOrUpdate(PokemonUserDataEntity(pokemonId, rating = rating))
        }
    }
    
    /**
     * Delete user data for a Pokemon.
     * Used when resetting to defaults.
     */
    @Query("DELETE FROM pokemon_user_data WHERE pokemonId = :pokemonId")
    suspend fun delete(pokemonId: Int)
    
    /**
     * Clear all user data.
     */
    @Query("DELETE FROM pokemon_user_data")
    suspend fun clearAll()
}

