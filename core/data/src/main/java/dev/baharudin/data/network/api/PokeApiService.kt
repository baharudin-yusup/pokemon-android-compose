package dev.baharudin.data.network.api

import dev.baharudin.data.remote.dto.PokemonDto
import dev.baharudin.data.remote.dto.PokemonListResponseDto
import dev.baharudin.data.remote.dto.TypeListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * PokeAPI service interface for Retrofit.
 *
 * Base URL: https://pokeapi.co/api/v2/
 */
internal interface PokeApiService {
    /**
     * Get paginated list of Pokemon.
     *
     * @param offset Number of Pokemon to skip
     * @param limit Number of Pokemon to return
     * @return PokemonListResponseDto with list of Pokemon
     */
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PokemonListResponseDto

    /**
     * Get Pokemon detail by ID.
     *
     * @param id Pokemon ID
     * @return PokemonDto with detailed Pokemon information
     */
    @GET("pokemon/{id}")
    suspend fun getPokemonById(
        @Path("id") id: Int
    ): PokemonDto

    /**
     * Get Pokemon detail by name.
     *
     * @param name Pokemon name
     * @return PokemonDto with detailed Pokemon information
     */
    @GET("pokemon/{name}")
    suspend fun getPokemonByName(
        @Path("name") name: String
    ): PokemonDto

    /**
     * Get all Pokemon types.
     *
     * @return TypeListResponseDto with list of all Pokemon types
     */
    @GET("type")
    suspend fun getAllTypes(): TypeListResponseDto
}

