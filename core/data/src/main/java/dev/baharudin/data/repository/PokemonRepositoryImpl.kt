package dev.baharudin.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dev.baharudin.common.util.Constants
import dev.baharudin.common.util.Result
import dev.baharudin.data.local.database.PokemonDatabase
import dev.baharudin.data.mapper.PokemonMapper
import dev.baharudin.data.network.api.PokeApiService
import dev.baharudin.data.paging.PokemonRemoteMediator
import dev.baharudin.domain.model.Pokemon
import dev.baharudin.domain.model.PokemonDetail
import dev.baharudin.domain.repository.PokemonRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

internal class PokemonRepositoryImpl
@Inject
constructor(
        private val apiService: PokeApiService,
        private val database: PokemonDatabase,
        private val remoteMediator: PokemonRemoteMediator
) : PokemonRepository {

    private val pokemonDao = database.pokemonDao()
    private val userDataDao = database.pokemonUserDataDao()
    private val backpackDao = database.pokemonBackpackDao()

    override fun getPokemonList(
            searchQuery: String,
            selectedTypes: Set<String>,
            sortBy: String
    ): Flow<List<Pokemon>> {
        val whereConditions = mutableListOf<String>()

        if (searchQuery.isNotEmpty()) {
            whereConditions.add("LOWER(name) LIKE '%${searchQuery.lowercase()}%'")
        }

        if (selectedTypes.isNotEmpty()) {
            val typeConditions =
                    selectedTypes.joinToString(" OR ") { type ->
                        "LOWER(types) LIKE '%${type.lowercase()}%'"
                    }
            whereConditions.add("($typeConditions)")
        }

        val whereClause =
                if (whereConditions.isNotEmpty()) {
                    "WHERE ${whereConditions.joinToString(" AND ")}"
                } else {
                    ""
                }

        val orderByClause =
                when (sortBy) {
                    "name_asc" -> "name ASC"
                    "name_desc" -> "name DESC"
                    "id_asc" -> "id ASC"
                    "id_desc" -> "id DESC"
                    "type" -> "types ASC"
                    else -> "id ASC"
                }

        val sqlQuery = "SELECT * FROM pokemon $whereClause ORDER BY $orderByClause"
        val query = androidx.sqlite.db.SimpleSQLiteQuery(sqlQuery)

        return pokemonDao.getPokemonListFlow(query).map { entities ->
            entities.map { entity -> PokemonMapper.toDomain(entity) }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPokemonListPaged(
            scope: CoroutineScope,
            searchQuery: String,
            selectedTypes: Set<String>,
            sortBy: String
    ): Flow<PagingData<Pokemon>> {
        // Build WHERE clause
        val whereConditions = mutableListOf<String>()

        // Add search condition
        if (searchQuery.isNotEmpty()) {
            whereConditions.add("LOWER(name) LIKE '%${searchQuery.lowercase()}%'")
        }

        // Add type filter condition
        if (selectedTypes.isNotEmpty()) {
            val typeConditions =
                    selectedTypes.joinToString(" OR ") { type ->
                        "LOWER(types) LIKE '%${type.lowercase()}%'"
                    }
            whereConditions.add("($typeConditions)")
        }

        val whereClause =
                if (whereConditions.isNotEmpty()) {
                    "WHERE ${whereConditions.joinToString(" AND ")}"
                } else {
                    ""
                }

        // Build ORDER BY clause
        val orderByClause =
                when (sortBy) {
                    "name_asc" -> "name ASC"
                    "name_desc" -> "name DESC"
                    "id_asc" -> "id ASC"
                    "id_desc" -> "id DESC"
                    "type" -> "types ASC"
                    else -> "id ASC"
                }

        val sqlQuery = "SELECT * FROM pokemon $whereClause ORDER BY $orderByClause"
        val query = androidx.sqlite.db.SimpleSQLiteQuery(sqlQuery)

        return Pager(
                        config =
                                PagingConfig(
                                        pageSize = Constants.Pagination.DEFAULT_PAGE_SIZE,
                                        enablePlaceholders = false,
                                        prefetchDistance = Constants.Pagination.PREFETCH_DISTANCE
                                ),
                        remoteMediator = remoteMediator,
                        pagingSourceFactory = { pokemonDao.getPokemonWithFilters(query) }
                )
                .flow
                .map { pagingData -> pagingData.map { entity -> PokemonMapper.toDomain(entity) } }
                .cachedIn(scope)
    }

    /**
     * Generate empty message based on search and filter parameters.
     *
     * @param searchQuery Current search query
     * @param selectedTypes Currently selected types
     * @return Appropriate empty message
     */
    override fun getEmptyMessage(searchQuery: String, selectedTypes: Set<String>): String {
        return when {
            searchQuery.isNotEmpty() && selectedTypes.isNotEmpty() ->
                    "No Pokemon named \"$searchQuery\" found with the selected types"
            searchQuery.isNotEmpty() -> "No Pokemon named \"$searchQuery\" found"
            selectedTypes.isNotEmpty() -> "No Pokemon found with the selected types"
            else -> "No Pokemon found"
        }
    }

    /**
     * Get paginated list of backpack Pokemon with PagingData using JOIN. Use this in feature
     * modules for efficient pagination with Paging 3.
     *
     * Uses JOIN between pokemon_backpack (IDs) and pokemon (full data) tables. Returns fresh
     * Pokemon data with latest stats, ratings, etc.
     *
     * @param scope Coroutine scope for caching PagingData
     * @return Flow of PagingData containing backpack Pokemon list
     */
    override fun getBackpackPokemonPaged(scope: CoroutineScope): Flow<PagingData<Pokemon>> {
        return Pager(
                        config =
                                PagingConfig(
                                        pageSize = Constants.Pagination.DEFAULT_PAGE_SIZE,
                                        enablePlaceholders = false,
                                        prefetchDistance = Constants.Pagination.PREFETCH_DISTANCE
                                ),
                        pagingSourceFactory = { backpackDao.getBackpackPokemonPaged() }
                )
                .flow
                .map { pagingData -> pagingData.map { entity -> PokemonMapper.toDomain(entity) } }
                .cachedIn(scope)
    }

    override suspend fun getPokemonDetail(id: Int): Result<PokemonDetail> {
        return try {
            // Try to get from database first
            val entity = pokemonDao.getPokemonById(id)
            if (entity != null) {
                val detail = PokemonMapper.toDomainDetail(entity)
                if (detail != null) {
                    return Result.Success(detail)
                }
            }

            // Fetch from network if not in database or incomplete
            val dto = apiService.getPokemonById(id)
            val domain = PokemonMapper.toDomain(dto)

            // Save to database
            val isInBackpack = entity?.isInBackpack ?: false
            val newEntity = PokemonMapper.toEntity(dto, isInBackpack)
            pokemonDao.insertPokemon(newEntity)

            Result.Success(domain)
        } catch (e: HttpException) {
            Result.Error(e)
        } catch (e: IOException) {
            Result.Error(e)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getPokemonDetail(name: String): Result<PokemonDetail> {
        return try {
            // Try to get from database first
            val searchResults = pokemonDao.searchPokemon(name)
            val entity = searchResults.firstOrNull()
            if (entity != null) {
                val detail = PokemonMapper.toDomainDetail(entity)
                if (detail != null) {
                    return Result.Success(detail)
                }
            }

            // Fetch from network if not in database or incomplete
            val dto = apiService.getPokemonByName(name.lowercase())
            val domain = PokemonMapper.toDomain(dto)

            // Save to database
            val isInBackpack = entity?.isInBackpack ?: false
            val newEntity = PokemonMapper.toEntity(dto, isInBackpack)
            pokemonDao.insertPokemon(newEntity)

            Result.Success(domain)
        } catch (e: HttpException) {
            Result.Error(e)
        } catch (e: IOException) {
            Result.Error(e)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getBackpackPokemon(): Flow<List<Pokemon>> {
        // Note: This method is kept for compatibility but getBackpackPokemonPaged should be used
        // instead
        return flow { emit(emptyList()) }
    }

    override suspend fun isPokemonInBackpack(pokemonId: Int): Boolean {
        return try {
            // Use separate backpack table
            backpackDao.isInBackpack(pokemonId)
        } catch (e: Exception) {
            false
        }
    }

    override fun searchPokemon(query: String): Flow<List<Pokemon>> {
        return flow {
            val entities = pokemonDao.searchPokemon(query)
            val pokemon = entities.map { entity -> PokemonMapper.toDomain(entity) }
            emit(pokemon)
        }
    }

    override suspend fun getAllPokemonTypes(): Result<List<String>> {
        return try {
            val response = apiService.getAllTypes()
            val types =
                    response.results.map { it.name.replaceFirstChar { char -> char.uppercase() } }
            Result.Success(types)
        } catch (e: HttpException) {
            Result.Error(e)
        } catch (e: IOException) {
            Result.Error(e)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateFavoriteStatus(pokemonId: Int, isFavorite: Boolean): Result<String> {
        return try {
            // Use separate user data table instead of pokemon table
            // This prevents PagingSource invalidation and eliminates flicker
            userDataDao.updateFavorite(pokemonId, isFavorite)

            val message = if (isFavorite) "Added to favorites" else "Removed from favorites"
            Result.Success(message)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun isPokemonFavorite(pokemonId: Int): Boolean {
        return try {
            // Use separate user data table
            userDataDao.isFavorite(pokemonId) ?: false
        } catch (e: Exception) {
            false
        }
    }

    override fun getFavoritePokemonIds(): Flow<Set<Int>> {
        // Use separate user data table
        return userDataDao.observeFavoritePokemonIds().map { it.toSet() }
    }

    override suspend fun getPokemonRating(pokemonId: Int): Int {
        return try {
            // Get rating from user data table (primary source)
            userDataDao.getRating(pokemonId) ?: 0
        } catch (e: Exception) {
            0
        }
    }

    override suspend fun updateRating(pokemonId: Int, rating: Int): Result<String> {
        return try {
            // Validate rating is between 0-5
            val validRating = rating.coerceIn(0, 5)

            // Update in user data table (primary source for rating)
            userDataDao.updateRating(pokemonId, validRating)

            // Also update in main pokemon table for caching
            // This allows JOIN queries to show latest rating
            pokemonDao.updateRating(pokemonId, validRating)

            val message = "Rated $validRating star${if (validRating != 1) "s" else ""}"
            Result.Success(message)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun addPokemonToBackpack(pokemonId: Int): Result<String> {
        return try {
            // Simply add Pokemon ID to backpack table
            // Full data will be fetched via JOIN when needed
            val backpackEntity =
                    dev.baharudin.data.local.entity.PokemonBackpackEntity(
                            pokemonId = pokemonId,
                            addedAt = System.currentTimeMillis()
                    )
            backpackDao.addToBackpack(backpackEntity)

            Result.Success("Added to backpack")
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun removePokemonFromBackpack(pokemonId: Int): Result<String> {
        return try {
            // Remove from separate backpack table
            backpackDao.removeFromBackpack(pokemonId)
            Result.Success("Removed from backpack")
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
