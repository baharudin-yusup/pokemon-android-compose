package dev.baharudin.common.util

/**
 * Application-wide constants.
 */
object Constants {
    /**
     * PokeAPI base URL
     */
    const val POKE_API_BASE_URL = "https://pokeapi.co/api/v2/"

    /**
     * Default page size for pagination
     */
    const val DEFAULT_PAGE_SIZE = 20

    /**
     * Pokemon image base URL
     */
    const val POKEMON_IMAGE_BASE_URL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"

    /**
     * Pokemon image URL format: {POKEMON_IMAGE_BASE_URL}{pokemonId}.png
     */
    fun getPokemonImageUrl(pokemonId: Int): String {
        return "${POKEMON_IMAGE_BASE_URL}${pokemonId}.png"
    }

    /**
     * Database name
     */
    const val DATABASE_NAME = "pokemon_database"

    /**
     * Database version
     */
    const val DATABASE_VERSION = 1
}

