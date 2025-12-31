package dev.baharudin.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.baharudin.common.util.Logger
import dev.baharudin.data.local.database.PokemonDatabase
import dev.baharudin.data.local.entity.PokemonEntity
import dev.baharudin.data.local.entity.PokemonRemoteKeysEntity
import dev.baharudin.data.mapper.PokemonMapper
import dev.baharudin.data.network.api.PokeApiService
import java.io.IOException
import retrofit2.HttpException

/**
 * RemoteMediator for Paging 3.
 *
 * Handles loading data from network and saving to database. Supports offline-first pagination with
 * RemoteMediator pattern.
 */
@OptIn(ExperimentalPagingApi::class)
internal class PokemonRemoteMediator(
        private val database: PokemonDatabase,
        private val apiService: PokeApiService,
        private val logger: Logger
) : RemoteMediator<Int, PokemonEntity>() {

    private val pokemonDao = database.pokemonDao()
    private val remoteKeysDao = database.remoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        // Check if database is empty
        val pokemonCount = pokemonDao.getPokemonCount()
        return if (pokemonCount == 0) {
            // Database is empty, refresh from network
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            // Database has data, use cached data
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
            loadType: LoadType,
            state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        return try {
            val offset =
                    when (loadType) {
                        LoadType.REFRESH -> {
                            // Clear database on refresh
                            database.withTransaction {
                                pokemonDao.clearAll()
                                remoteKeysDao.clearRemoteKeys()
                            }
                            0
                        }
                        LoadType.PREPEND -> {
                            // Not supported - we only append
                            return MediatorResult.Success(endOfPaginationReached = true)
                        }
                        LoadType.APPEND -> {
                            val remoteKeys = getRemoteKeyForLastItem(state)
                            val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                                endOfPaginationReached = true
                            )
                            nextKey
                        }
                    }

            // Fetch from network
            val response = apiService.getPokemonList(offset = offset, limit = state.config.pageSize)

            // Extract Pokemon IDs from list response and fetch details
            val pokemonList = mutableListOf<PokemonEntity>()
            response.results.forEach { item ->
                try {
                    val pokemonId = extractIdFromUrl(item.url)
                    val pokemonDetail = apiService.getPokemonById(pokemonId)
                    val entity = PokemonMapper.toEntity(pokemonDetail)
                    pokemonList.add(entity)
                } catch (e: Exception) {
                    logger.e(
                            "PokemonRemoteMediator",
                            e,
                            "Failed to fetch Pokemon detail for ${item.name}"
                    )
                    // Fallback to basic entity
                    val entity = PokemonMapper.toEntity(item)
                    pokemonList.add(entity)
                }
            }

            val endOfPaginationReached = response.next == null

            database.withTransaction {
                // Save remote keys for pagination
                val remoteKeys =
                        pokemonList.map { entity ->
                            PokemonRemoteKeysEntity(
                                    pokemonId = entity.id,
                                    prevKey =
                                            if (offset == 0) null
                                            else offset - state.config.pageSize,
                                    nextKey =
                                            if (endOfPaginationReached) null
                                            else offset + state.config.pageSize
                            )
                        }
                remoteKeysDao.insertAll(remoteKeys)

                // Save Pokemon entities
                pokemonDao.insertAllPokemon(pokemonList)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            logger.e("PokemonRemoteMediator", e, "Network error loading Pokemon list")
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            logger.e("PokemonRemoteMediator", e, "HTTP error loading Pokemon list")
            MediatorResult.Error(e)
        } catch (e: Exception) {
            logger.e("PokemonRemoteMediator", e, "Unexpected error loading Pokemon list")
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
            state: PagingState<Int, PokemonEntity>
    ): PokemonRemoteKeysEntity? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { entity
            ->
            remoteKeysDao.remoteKeysByPokemonId(entity.id)
        }
    }

    private fun extractIdFromUrl(url: String): Int {
        return try {
            val trimmed = url.trimEnd { it == '/' }
            trimmed.substringAfterLast('/').toInt()
        } catch (e: Exception) {
            0
        }
    }
}
