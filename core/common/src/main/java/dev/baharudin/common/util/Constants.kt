package dev.baharudin.common.util

object Constants {
    object Pagination {
        const val DEFAULT_PAGE_SIZE = 20
        const val PREFETCH_DISTANCE = 5
    }
    
    object Images {
        private const val BASE_URL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"
        
        fun getPokemonImageUrl(pokemonId: Int): String {
            return "${BASE_URL}${pokemonId}.png"
        }
    }
    
    object Database {
        const val NAME = "pokemon_database"
        const val VERSION = 6
    }
}

