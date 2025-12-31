package dev.baharudin.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PokemonColors.Primary,
    secondary = PokemonColors.Secondary,
    background = androidx.compose.ui.graphics.Color(0xFF1F2937),
    surface = androidx.compose.ui.graphics.Color(0xFF374151),
    error = PokemonColors.Error
)

private val LightColorScheme = lightColorScheme(
    primary = PokemonColors.Primary,
    secondary = PokemonColors.Secondary,
    background = PokemonColors.Background,
    surface = PokemonColors.Surface,
    error = PokemonColors.Error
)

@Composable
fun PokemonComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PokemonTypography,
        content = content
    )
}
