package dev.baharudin.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import dev.baharudin.domain.model.Pokemon

@Composable
fun PokemonList(
    lazyPagingItems: LazyPagingItems<Pokemon>,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
    columns: Int = 2,
    emptyMessage: String = "No pokemon found",
    lazyGridState: LazyGridState = rememberLazyGridState(),
    itemContent: @Composable (pokemon: Pokemon, index: Int) -> Unit
) {
    when (val loadState = lazyPagingItems.loadState.refresh) {
        is LoadState.Loading -> {
            LoadingIndicator(modifier = modifier)
        }

        is LoadState.Error -> {
            ErrorScreen(
                message = loadState.error.message ?: "Failed to load items",
                onRetry = onRetry,
                modifier = modifier
            )
        }

        is LoadState.NotLoading -> {
            if (lazyPagingItems.itemCount == 0) {
                EmptyState(message = emptyMessage, modifier = modifier)
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    state = lazyGridState,
                    modifier = modifier.fillMaxSize(),
                    contentPadding = contentPadding,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        count = lazyPagingItems.itemCount,
                        key = lazyPagingItems.itemKey { it.id }
                    ) { index ->
                        val pokemon = lazyPagingItems[index]
                        if (pokemon != null) {
                            Box(
                                modifier = Modifier
                                    .zIndex(0f)
                                    .animateItem(
                                        fadeInSpec = tween(durationMillis = 250),
                                        fadeOutSpec = tween(durationMillis = 200),
                                        placementSpec = spring(
                                            dampingRatio = Spring.DampingRatioNoBouncy,
                                            stiffness = Spring.StiffnessMediumLow
                                        )
                                    )
                            ) {
                                itemContent(pokemon, index)
                            }
                        }
                    }

                    when (val appendState = lazyPagingItems.loadState.append) {
                        is LoadState.Loading -> {
                            item(span = { GridItemSpan(columns) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        is LoadState.Error -> {
                            item(span = { GridItemSpan(columns) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Failed to load more",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}

