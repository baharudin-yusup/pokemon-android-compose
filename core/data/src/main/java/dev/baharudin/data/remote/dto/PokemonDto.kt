package dev.baharudin.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PokemonDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("height")
    val height: Int,
    @SerialName("weight")
    val weight: Int,
    @SerialName("sprites")
    val sprites: PokemonSpritesDto,
    @SerialName("types")
    val types: List<PokemonTypeSlotDto>,
    @SerialName("stats")
    val stats: List<PokemonStatDto>,
    @SerialName("abilities")
    val abilities: List<PokemonAbilitySlotDto>
)

@Serializable
internal data class PokemonSpritesDto(
    @SerialName("other")
    val other: PokemonOtherSpritesDto? = null
)

@Serializable
internal data class PokemonOtherSpritesDto(
    @SerialName("official-artwork")
    val officialArtwork: PokemonOfficialArtworkDto? = null
)

@Serializable
internal data class PokemonOfficialArtworkDto(
    @SerialName("front_default")
    val frontDefault: String? = null
)

@Serializable
internal data class PokemonTypeSlotDto(
    @SerialName("slot")
    val slot: Int,
    @SerialName("type")
    val type: PokemonTypeInfoDto
)

@Serializable
internal data class PokemonTypeInfoDto(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String
)

@Serializable
internal data class PokemonStatDto(
    @SerialName("base_stat")
    val baseStat: Int,
    @SerialName("stat")
    val stat: PokemonStatInfoDto
)

@Serializable
internal data class PokemonStatInfoDto(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String
)

@Serializable
internal data class PokemonAbilitySlotDto(
    @SerialName("ability")
    val ability: PokemonAbilityInfoDto,
    @SerialName("is_hidden")
    val isHidden: Boolean,
    @SerialName("slot")
    val slot: Int
)

@Serializable
internal data class PokemonAbilityInfoDto(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String
)
