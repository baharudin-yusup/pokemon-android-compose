package dev.baharudin.data.mapper

import dev.baharudin.common.util.Constants
import dev.baharudin.data.local.entity.PokemonEntity
import dev.baharudin.data.remote.dto.PokemonDto
import dev.baharudin.data.remote.dto.PokemonListItemDto
import dev.baharudin.domain.model.Pokemon
import dev.baharudin.domain.model.PokemonDetail
import dev.baharudin.domain.model.PokemonStats

/** Mapper functions to convert between data layer models and domain models. */
internal object PokemonMapper {
    /** Convert PokemonDto to PokemonDetail domain model. */
    fun toDomain(dto: PokemonDto): PokemonDetail {
        val imageUrl =
                dto.sprites.other?.officialArtwork?.frontDefault
                        ?: Constants.Images.getPokemonImageUrl(dto.id)

        val stats = extractStats(dto.stats)
        val types = dto.types.map { it.type.name }
        val abilities = dto.abilities.map { it.ability.name }

        return PokemonDetail(
                id = dto.id,
                name = dto.name,
                imageUrl = imageUrl,
                height = dto.height,
                weight = dto.weight,
                types = types,
                stats = stats,
                abilities = abilities
        )
    }

    /** Convert PokemonDto to Pokemon (list item) domain model. */
    fun toDomainListItem(dto: PokemonDto): Pokemon {
        val imageUrl =
                dto.sprites.other?.officialArtwork?.frontDefault
                        ?: Constants.Images.getPokemonImageUrl(dto.id)

        val types = dto.types.map { it.type.name }

        return Pokemon(id = dto.id, name = dto.name, imageUrl = imageUrl, types = types)
    }

    /**
     * Convert PokemonListItemDto to PokemonEntity. Note: This extracts ID from URL since list
     * endpoint doesn't provide ID.
     */
    fun toEntity(item: PokemonListItemDto): PokemonEntity {
        // Extract ID from URL: "https://pokeapi.co/api/v2/pokemon/1/"
        val id = extractIdFromUrl(item.url)

        return PokemonEntity(
                id = id,
                name = item.name,
                imageUrl = Constants.Images.getPokemonImageUrl(id),
                height = 0,
                weight = 0,
                types = emptyList(),
                hp = null,
                attack = null,
                defense = null,
                specialAttack = null,
                specialDefense = null,
                speed = null,
                abilities = emptyList(),
                isInBackpack = false
        )
    }

    /** Convert PokemonDto to PokemonEntity. */
    fun toEntity(dto: PokemonDto, isInBackpack: Boolean = false): PokemonEntity {
        val imageUrl =
                dto.sprites.other?.officialArtwork?.frontDefault
                        ?: Constants.Images.getPokemonImageUrl(dto.id)

        val types = dto.types.map { it.type.name }
        val stats = extractStats(dto.stats)
        val abilities = dto.abilities.map { it.ability.name }

        return PokemonEntity(
                id = dto.id,
                name = dto.name,
                imageUrl = imageUrl,
                height = dto.height,
                weight = dto.weight,
                types = types,
                hp = stats.hp,
                attack = stats.attack,
                defense = stats.defense,
                specialAttack = stats.specialAttack,
                specialDefense = stats.specialDefense,
                speed = stats.speed,
                abilities = abilities,
                isInBackpack = isInBackpack
        )
    }

    /** Convert PokemonEntity to Pokemon domain model. */
    fun toDomain(entity: PokemonEntity): Pokemon {
        return Pokemon(
                id = entity.id,
                name = entity.name,
                imageUrl = entity.imageUrl,
                types = entity.types,
                isFavorite = entity.isFavorite,
                rating = entity.rating
        )
    }

    /** Convert PokemonEntity to PokemonDetail domain model. */
    fun toDomainDetail(entity: PokemonEntity): PokemonDetail? {
        if (entity.hp == null ||
                        entity.attack == null ||
                        entity.defense == null ||
                        entity.specialAttack == null ||
                        entity.specialDefense == null ||
                        entity.speed == null
        ) {
            return null
        }

        return PokemonDetail(
                id = entity.id,
                name = entity.name,
                imageUrl = entity.imageUrl,
                height = entity.height,
                weight = entity.weight,
                types = entity.types,
                stats =
                        PokemonStats(
                                hp = entity.hp,
                                attack = entity.attack,
                                defense = entity.defense,
                                specialAttack = entity.specialAttack,
                                specialDefense = entity.specialDefense,
                                speed = entity.speed
                        ),
                abilities = entity.abilities
        )
    }

    /** Extract Pokemon ID from PokeAPI URL. Format: "https://pokeapi.co/api/v2/pokemon/{id}/" */
    private fun extractIdFromUrl(url: String): Int {
        return try {
            val trimmed = url.trimEnd { it == '/' }
            trimmed.substringAfterLast('/').toInt()
        } catch (e: Exception) {
            0
        }
    }

    /** Extract stats from PokemonDto stats list. */
    private fun extractStats(
            stats: List<dev.baharudin.data.remote.dto.PokemonStatDto>
    ): PokemonStats {
        var hp = 0
        var attack = 0
        var defense = 0
        var specialAttack = 0
        var specialDefense = 0
        var speed = 0

        stats.forEach { stat ->
            when (stat.stat.name) {
                "hp" -> hp = stat.baseStat
                "attack" -> attack = stat.baseStat
                "defense" -> defense = stat.baseStat
                "special-attack" -> specialAttack = stat.baseStat
                "special-defense" -> specialDefense = stat.baseStat
                "speed" -> speed = stat.baseStat
            }
        }

        return PokemonStats(
                hp = hp,
                attack = attack,
                defense = defense,
                specialAttack = specialAttack,
                specialDefense = specialDefense,
                speed = speed
        )
    }
}
