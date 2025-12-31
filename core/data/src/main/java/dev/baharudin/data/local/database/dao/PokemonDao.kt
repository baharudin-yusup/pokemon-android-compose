package dev.baharudin.data.local.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.baharudin.data.local.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Pokemon database operations.
 */
@Dao
internal interface PokemonDao {
    @Query("SELECT * FROM pokemon ORDER BY id ASC")
    fun getAllPokemon(): PagingSource<Int, PokemonEntity>

    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun getPokemonById(id: Int): PokemonEntity?

    /**
     * Get all Pokemon with custom filtering and sorting using raw query.
     * Supports search by name and filter by types.
     * 
     * @param query SQL query with WHERE and ORDER BY clauses
     */
    @androidx.room.RawQuery(observedEntities = [PokemonEntity::class])
    fun getPokemonWithFilters(query: androidx.sqlite.db.SupportSQLiteQuery): PagingSource<Int, PokemonEntity>

    @androidx.room.RawQuery(observedEntities = [PokemonEntity::class])
    fun getPokemonListFlow(query: androidx.sqlite.db.SupportSQLiteQuery): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon WHERE isInBackpack = 1 ORDER BY id ASC")
    suspend fun getBackpackPokemon(): List<PokemonEntity>

    @Query("SELECT * FROM pokemon WHERE isInBackpack = 1 ORDER BY id ASC")
    fun observeBackpackPokemon(): Flow<List<PokemonEntity>>

    @androidx.room.RawQuery(observedEntities = [PokemonEntity::class])
    fun getBackpackPokemonPaged(query: androidx.sqlite.db.SupportSQLiteQuery): PagingSource<Int, PokemonEntity>

    @Query("SELECT id FROM pokemon WHERE isInBackpack = 1")
    fun observeBackpackPokemonIds(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPokemon(pokemon: List<PokemonEntity>)

    @Update
    suspend fun updatePokemon(pokemon: PokemonEntity)

    @Query("UPDATE pokemon SET isInBackpack = :isInBackpack WHERE id = :id")
    suspend fun updateBackpackStatus(id: Int, isInBackpack: Boolean)

    @Query("UPDATE pokemon SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)

    @Query("UPDATE pokemon SET rating = :rating WHERE id = :id")
    suspend fun updateRating(id: Int, rating: Int)

    @Query("SELECT id FROM pokemon WHERE isFavorite = 1")
    fun observeFavoritePokemonIds(): Flow<List<Int>>

    @Query("DELETE FROM pokemon")
    suspend fun clearAll()

    /**
     * Get the last updated timestamp from the first Pokemon.
     * Used to determine if cache is stale.
     */
    @Query("SELECT COUNT(*) FROM pokemon")
    suspend fun getPokemonCount(): Int

    /**
     * Get Pokemon by name (case-insensitive search).
     */
    @Query("SELECT * FROM pokemon WHERE LOWER(name) LIKE LOWER('%' || :query || '%') ORDER BY id ASC")
    suspend fun searchPokemon(query: String): List<PokemonEntity>
}

