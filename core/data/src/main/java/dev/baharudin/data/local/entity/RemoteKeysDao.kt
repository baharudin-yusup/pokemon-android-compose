package dev.baharudin.data.local.entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * DAO for Pokemon remote keys used in Paging 3 RemoteMediator.
 */
@Dao
internal interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<PokemonRemoteKeysEntity>)

    @Query("SELECT * FROM pokemon_remote_keys WHERE pokemonId = :pokemonId")
    suspend fun remoteKeysByPokemonId(pokemonId: Int): PokemonRemoteKeysEntity?

    @Query("DELETE FROM pokemon_remote_keys")
    suspend fun clearRemoteKeys()
}

