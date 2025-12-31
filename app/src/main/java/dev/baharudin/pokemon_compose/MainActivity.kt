package dev.baharudin.pokemon_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dagger.hilt.android.AndroidEntryPoint
import dev.baharudin.pokemon_compose.navigation.AppNavigation
import dev.baharudin.ui.theme.PokemonComposeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { PokemonComposeApp() }
    }
}

@PreviewScreenSizes
@Composable
fun PokemonComposeApp() {
    PokemonComposeTheme { AppNavigation() }
}