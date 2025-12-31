package dev.baharudin.detail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.baharudin.detail.ui.components.PokemonActions
import dev.baharudin.detail.ui.components.PokemonHeader
import dev.baharudin.detail.ui.components.PokemonRating
import dev.baharudin.detail.ui.components.PokemonStats
import dev.baharudin.common.util.onError
import dev.baharudin.common.util.onSuccess
import dev.baharudin.detail.ui.components.PokemonTypes
import dev.baharudin.ui.components.ErrorScreen
import dev.baharudin.ui.components.LoadingIndicator
import dev.baharudin.ui.util.rememberToastHandler
import kotlinx.coroutines.launch

/**
 * Detail screen for displaying Pokemon information.
 *
 * Features:
 * - Display Pokemon image and basic info (name, ID)
 * - Display Pokemon types
 * - Display Pokemon stats with progress bars
 * - Display Pokemon abilities
 * - Add/Remove from backpack functionality
 * - Loading and error states
 * - Sticky TopAppBar with back button
 *
 * @param pokemonId Pokemon ID to display (if provided, will load by ID)
 * @param pokemonName Pokemon name to display (if provided, will load by name)
 * @param onBackClick Callback when back button is clicked
 * @param modifier Modifier for the screen
 * @param viewModel DetailViewModel injected via Hilt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    pokemonId: Int? = null,
    pokemonName: String? = null,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isInBackpack by viewModel.isInBackpack.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val rating by viewModel.rating.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val showToast = rememberToastHandler()

    // Load Pokemon detail when screen is created
    LaunchedEffect(pokemonId, pokemonName) {
        when {
            pokemonId != null -> viewModel.loadPokemonDetail(pokemonId)
            pokemonName != null -> viewModel.loadPokemonDetail(pokemonName)
        }
    }

    // Setup scroll behavior for TopAppBar
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Pokemon Detail") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val currentState = uiState) {
                is DetailUiState.Initial -> {
                    // Initial state - nothing to show yet
                }

                is DetailUiState.Loading -> {
                    LoadingIndicator()
                }

                is DetailUiState.Success -> {
                    val pokemonDetail = currentState.pokemonDetail

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        // Pokemon header (image, name, ID)
                        PokemonHeader(
                            pokemonName = pokemonDetail.name,
                            pokemonId = pokemonDetail.id,
                            imageUrl = pokemonDetail.imageUrl,
                            modifier = Modifier.padding(top = 16.dp)
                        )

                        // Pokemon rating section
                        PokemonRating(
                            rating = rating,
                            onRatingChange = { newRating ->
                                coroutineScope.launch {
                                    viewModel.updateRating(pokemonDetail.id, newRating)
                                        .onSuccess { message -> showToast(message) }
                                        .onError { error -> showToast("Failed: ${error.message}") }
                                }
                            }
                        )

                        // Pokemon stats
                        SectionTitle(title = "Stats")
                        PokemonStats(stats = pokemonDetail.stats)

                        // Pokemon types
                        SectionTitle(title = "Types")
                        PokemonTypes(types = pokemonDetail.types)

                        // Pokemon actions
                        SectionTitle(title = "Actions")
                        PokemonActions(
                            isFavorite = isFavorite,
                            isInBackpack = isInBackpack,
                            onToggleFavorite = {
                                coroutineScope.launch {
                                    viewModel.toggleFavorite(pokemonDetail.id)
                                        .onSuccess { message -> showToast(message) }
                                        .onError { error -> showToast("Failed: ${error.message}") }
                                }
                            },
                            onToggleBackpack = {
                                coroutineScope.launch {
                                    viewModel.toggleBackpack(pokemonDetail.id)
                                        .onSuccess { message -> showToast(message) }
                                        .onError { error -> showToast("Failed: ${error.message}") }
                                }
                            }
                        )
                    }
                }

                is DetailUiState.Error -> {
                    ErrorScreen(
                        message = currentState.message,
                        onRetry = {
                            when {
                                pokemonId != null -> viewModel.loadPokemonDetail(pokemonId)
                                pokemonName != null -> viewModel.loadPokemonDetail(pokemonName)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

/** Section title component for Detail screen. */
@Composable
private fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 16.dp)
    )
}
