package dev.baharudin.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PokemonListResponseDto(
    @SerialName("count")
    val count: Int,
    @SerialName("next")
    val next: String? = null,
    @SerialName("previous")
    val previous: String? = null,
    @SerialName("results")
    val results: List<PokemonListItemDto>
)

@Serializable
internal data class PokemonListItemDto(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String
)
