package dev.baharudin.pokemon_compose.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes using Kotlin Serialization.
 *
 * Implements [NavKey] for Navigation Compose with serializable route definitions. Routes can
 * contain navigation arguments passed as constructor parameters.
 */
sealed interface Route : NavKey {
    /** Catalog screen. */
    @Serializable data object Catalog : Route

    /** Backpack screen. */
    @Serializable data object Backpack : Route

    /** Pokemon detail screen with Pokemon ID. */
    @Serializable data class Detail(val pokemonId: Int) : Route
}
