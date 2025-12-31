package dev.baharudin.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TypeListResponseDto(
    @SerialName("count")
    val count: Int,
    @SerialName("results")
    val results: List<TypeDto>
)

@Serializable
internal data class TypeDto(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String
)
