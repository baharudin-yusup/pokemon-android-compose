package dev.baharudin.backpack.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module for Backpack feature.
 *
 * Currently empty as ViewModel injection is handled by @HiltViewModel annotation.
 * Can be used for feature-specific dependencies in the future.
 */
@Module
@InstallIn(SingletonComponent::class)
object BackpackModule

