package dev.baharudin.domain.model

/**
 * Domain model for Pokemon detail.
 *
 * This represents a Pokemon with all detailed information.
 */
data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val height: Int,
    val weight: Int,
    val types: List<String>,
    val stats: PokemonStats,
    val abilities: List<String>
)

/**
 * Pokemon stats (base stats).
 */
data class PokemonStats(
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int
)

