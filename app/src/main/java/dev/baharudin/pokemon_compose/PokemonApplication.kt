package dev.baharudin.pokemon_compose

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Pokemon Compose Application class.
 */
@HiltAndroidApp
class PokemonApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Timber for logging
        // In debug builds, BuildConfig.DEBUG will be true
        initLogger(debugMode = true)
    }
}
