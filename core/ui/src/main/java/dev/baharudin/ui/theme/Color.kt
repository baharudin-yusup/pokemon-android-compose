package dev.baharudin.ui.theme

import androidx.compose.ui.graphics.Color

/** Pokemon Compose color palette. */
object PokemonColors {
    // Primary colors - Pokemon themed
    val Primary = Color(0xFF3B82F6) // Blue
    val PrimaryVariant = Color(0xFF2563EB)
    val Secondary = Color(0xFF10B981) // Green
    val SecondaryVariant = Color(0xFF059669)

    // Background colors
    val Background = Color(0xFFF9FAFB)
    val Surface = Color(0xFFFFFFFF)
    val SurfaceVariant = Color(0xFFF3F4F6)

    // Text colors
    val OnPrimary = Color(0xFFFFFFFF)
    val OnSecondary = Color(0xFFFFFFFF)
    val OnBackground = Color(0xFF111827)
    val OnSurface = Color(0xFF111827)
    val OnSurfaceVariant = Color(0xFF6B7280)

    // Error colors
    val Error = Color(0xFFEF4444)
    val OnError = Color(0xFFFFFFFF)

    // Pokemon type colors
    val TypeNormal = Color(0xFFA8A878)
    val TypeFire = Color(0xFFF08030)
    val TypeWater = Color(0xFF6890F0)
    val TypeElectric = Color(0xFFF8D030)
    val TypeGrass = Color(0xFF78C850)
    val TypeIce = Color(0xFF98D8D8)
    val TypeFighting = Color(0xFFC03028)
    val TypePoison = Color(0xFFA040A0)
    val TypeGround = Color(0xFFE0C068)
    val TypeFlying = Color(0xFFA890F0)
    val TypePsychic = Color(0xFFF85888)
    val TypeBug = Color(0xFFA8B820)
    val TypeRock = Color(0xFFB8A038)
    val TypeGhost = Color(0xFF705898)
    val TypeDragon = Color(0xFF7038F8)
    val TypeDark = Color(0xFF705848)
    val TypeSteel = Color(0xFFB8B8D0)
    val TypeFairy = Color(0xFFEE99AC)
}

/** Get Pokemon type color by type name. */
fun getPokemonTypeColor(typeName: String): Color {
    return when (typeName.lowercase()) {
        "normal" -> PokemonColors.TypeNormal
        "fire" -> PokemonColors.TypeFire
        "water" -> PokemonColors.TypeWater
        "electric" -> PokemonColors.TypeElectric
        "grass" -> PokemonColors.TypeGrass
        "ice" -> PokemonColors.TypeIce
        "fighting" -> PokemonColors.TypeFighting
        "poison" -> PokemonColors.TypePoison
        "ground" -> PokemonColors.TypeGround
        "flying" -> PokemonColors.TypeFlying
        "psychic" -> PokemonColors.TypePsychic
        "bug" -> PokemonColors.TypeBug
        "rock" -> PokemonColors.TypeRock
        "ghost" -> PokemonColors.TypeGhost
        "dragon" -> PokemonColors.TypeDragon
        "dark" -> PokemonColors.TypeDark
        "steel" -> PokemonColors.TypeSteel
        "fairy" -> PokemonColors.TypeFairy
        else -> PokemonColors.TypeNormal
    }
}

