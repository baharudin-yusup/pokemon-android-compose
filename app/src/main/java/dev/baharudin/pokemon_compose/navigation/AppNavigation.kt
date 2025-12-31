package dev.baharudin.pokemon_compose.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.baharudin.catalog.ui.CatalogScreen
import dev.baharudin.detail.ui.DetailScreen
import dev.baharudin.pokemon_compose.ui.AppDestinations
import dev.baharudin.pokemon_compose.ui.HomeScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(Route.Catalog)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() }, entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = {
            ContentTransform(
                targetContentEnter = slideInHorizontally(initialOffsetX = { it }),
                initialContentExit = slideOutHorizontally(targetOffsetX = { -it / 2 })
            )
        },
        popTransitionSpec = {
            ContentTransform(
                targetContentEnter = slideInHorizontally(initialOffsetX = { -it / 2 }),
                initialContentExit = slideOutHorizontally(targetOffsetX = { it })
            )
        }
    ) { key ->
        when (key) {
            is Route.Catalog -> {
                NavEntry(key = key) {
                    HomeScreen(
                        onPokemonClick = { pokemon ->
                            backStack.add(Route.Detail(pokemon.id))
                        }, initialDestination = AppDestinations.CATALOG, modifier = modifier
                    )
                }
            }

            is Route.Backpack -> {
                NavEntry(key = key) {
                    HomeScreen(
                        onPokemonClick = { pokemon ->
                            backStack.add(Route.Detail(pokemon.id))
                        }, initialDestination = AppDestinations.BACKPACK, modifier = modifier
                    )
                }
            }

            is Route.Detail -> {
                NavEntry(key = key) {
                    DetailScreen(
                        pokemonId = key.pokemonId, onBackClick = {
                            backStack.removeLastOrNull()
                        })
                }
            }

            else -> {
                NavEntry(key = key) {
                    CatalogScreen(
                        onPokemonClick = { pokemon ->
                            backStack.add(Route.Detail(pokemon.id))
                        }, modifier = modifier
                    )
                }
            }
        }
    }
}

