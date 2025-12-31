package dev.baharudin.data.local.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.baharudin.data.local.entity.PokemonBackpackEntity
import dev.baharudin.data.local.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Pokemon backpack operations using JOIN.
 * 
 * This approach uses a simple backpack table (ID only) and JOINs with
 * the main pokemon table to get full data.
 */
@Dao
internal interface PokemonBackpackDao {
    
    /**
     * Get all backpack Pokemon with pagination using JOIN.
     * Returns full Pokemon data by joining with pokemon table.
     * Ordered by addedAt descending (newest first).
     */
    @Query("""
        SELECT p.* 
        FROM pokemon p
        INNER JOIN pokemon_backpack b ON p.id = b.pokemonId
        ORDER BY b.addedAt DESC
    """)
    fun getBackpackPokemonPaged(): PagingSource<Int, PokemonEntity>
    
    /**
     * Get all backpack Pokemon IDs.
     * Used to check if a Pokemon is in backpack without loading full data.
     */
    @Query("SELECT pokemonId FROM pokemon_backpack")
    fun observeBackpackPokemonIds(): Flow<List<Int>>
    
    /**
     * Check if a Pokemon is in backpack.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM pokemon_backpack WHERE pokemonId = :pokemonId)")
    suspend fun isInBackpack(pokemonId: Int): Boolean
    
    /**
     * Add Pokemon to backpack (ID only).
     * Full data comes from pokemon table via JOIN.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToBackpack(backpack: PokemonBackpackEntity)
    
    /**
     * Remove Pokemon from backpack.
     */
    @Query("DELETE FROM pokemon_backpack WHERE pokemonId = :pokemonId")
    suspend fun removeFromBackpack(pokemonId: Int)
    
    /**
     * Get backpack count.
     */
    @Query("SELECT COUNT(*) FROM pokemon_backpack")
    suspend fun getBackpackCount(): Int
    
    /**
     * Clear all backpack Pokemon.
     */
    @Query("DELETE FROM pokemon_backpack")
    suspend fun clearAll()
}

