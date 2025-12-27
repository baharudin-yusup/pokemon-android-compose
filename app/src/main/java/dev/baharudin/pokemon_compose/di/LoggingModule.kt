package dev.baharudin.pokemon_compose.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.baharudin.common.util.Logger
import dev.baharudin.pokemon_compose.TimberLogger
import javax.inject.Singleton

/**
 * Hilt module for providing logging dependencies.
 *
 * This module provides Logger implementation using Timber. Logger should be injected via @Inject
 * constructor in Android modules.
 */
@Module
@InstallIn(SingletonComponent::class)
object LoggingModule {

    @Provides
    @Singleton
    fun provideLogger(): Logger {
        return TimberLogger()
    }
}
